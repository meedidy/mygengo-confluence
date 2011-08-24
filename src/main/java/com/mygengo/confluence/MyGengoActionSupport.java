package com.mygengo.confluence;

import java.util.*;
import com.atlassian.confluence.core.ConfluenceActionSupport;
import com.atlassian.bandana.BandanaManager;
import com.atlassian.confluence.setup.bandana.ConfluenceBandanaContext;
import com.opensymphony.webwork.ServletActionContext;
import com.atlassian.confluence.web.context.HttpContext;

/**
 * Base class for job management actions.
 */
public class MyGengoActionSupport extends ConfluenceActionSupport
{
	protected HttpContext mHttpContext;
	protected TranslationService mTranslationService;
	protected Database mDatabase;
	protected Job mJob;
	protected Credentials mCredentials;

	public void setTranslationService(TranslationService s)	{ mTranslationService = s; }
	public void setDatabase(Database s) 										{ mDatabase = s; }
	public void setHttpContext(HttpContext h) 							{ mHttpContext = h; }

	public List<Job> getJobs() { return mDatabase.all(); }
	public Job getJob() { return mJob; }

	public String execute()
	{ 
		mCredentials = mDatabase.credentials();

		String jid = mHttpContext.getRequest().getParameter("jid");
		if(jid != null && new Integer(jid) > 0)
		{
			mJob = mDatabase.job(new Integer(mHttpContext.getRequest().getParameter("jid")));
			return "success";
		}
		else
		{
			mJob = null;
			return "error";
		}
	}
}
