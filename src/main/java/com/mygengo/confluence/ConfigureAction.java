package com.mygengo.confluence;

import java.util.*;
import com.atlassian.confluence.core.ConfluenceActionSupport;
import com.atlassian.bandana.BandanaManager;
import com.atlassian.confluence.setup.bandana.ConfluenceBandanaContext;
import com.opensymphony.webwork.ServletActionContext;
import com.atlassian.confluence.web.context.HttpContext;

/**
 * Display the config form of the plugin.
 * Allow the user to save his api/private key.
 */
public class ConfigureAction extends ConfluenceActionSupport
{
		private HttpContext httpContext;
		private TranslationService mTranslationService;
		private Database mDatabase;

		public void setTranslationService(TranslationService s)	{ mTranslationService = s; }
		public void setDatabase(Database s) 										{ mDatabase = s; }
		public void setHttpContext(HttpContext h) 							{ httpContext = h; }

		public Credentials getCredentials() 										{ return mDatabase.credentials(); }

		public String doView()
		{
			return "success";
		}

		public String doSave()
		{ 
			Credentials cred = mDatabase.credentials();
			cred.setApiKey(httpContext.getRequest().getParameter("apikey"));
			cred.setPrivKey(httpContext.getRequest().getParameter("privkey"));

			try
			{
				addActionMessage("Balance: " + mTranslationService.balance(cred));

				mDatabase.purge();

				List<Job> l = mTranslationService.list(cred);
				Iterator<Job> i = l.iterator();
				while(i.hasNext())
				{
					Job j = i.next();
					try
					{
						j.setThread(mTranslationService.thread(cred,j));
						if(mDatabase.merge(j))
							addActionMessage("New Job #" + j.getJobId() + " (" + j.getStatus() + ") with " + j.getThread().size() + " comments");
					}
					catch(RuntimeException e)
					{
						addActionError("Failed to add new Job #" + j.getJobId() + " (" + e.getMessage() + ")");
					}
				}
				addActionMessage("Processed " + l.size() + " jobs");
				
				mDatabase.merge(cred);
				addActionMessage("Credentials saved.");
			}
			catch(RuntimeException e)
			{
				addActionError(e.getMessage());
				addActionError("Credentials invalid!");
			}

			return "success";
		}
}
