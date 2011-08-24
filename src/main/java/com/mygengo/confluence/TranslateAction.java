package com.mygengo.confluence;

import com.atlassian.confluence.spaces.*;
import com.atlassian.confluence.pages.*;
import com.atlassian.confluence.languages.*;
import java.util.*;

import com.atlassian.confluence.json.parser.*;

/**
 * Translate form for ordering.
 */
public class TranslateAction extends MyGengoActionSupport
{
	private SpaceManager mSpaceManager;
	private PageManager mPageManager;
	private LocaleManager mLocaleManager;
	private Page mPage;

	public void setSpaceManager(SpaceManager s) { mSpaceManager = s; }	
	public void setPageManager(PageManager s) { mPageManager = s; }	
	public void setLocaleManager(LocaleManager s) { mLocaleManager = s; }	

	public String getBody()
	{
		if(mPage != null)
			return mPage.getContent().replaceAll("(h[1-6]\\.)|(\\{.+?\\})|(\\[)|(\\|.+?\\])","");
		else
			return "no page";
	}

	public String getSlug()
	{
		if(mPage != null)
			return mPage.getDisplayTitle();
		else
			return "no page";
	}

	public String getDefaultLang()
	{
		return mLocaleManager.DEFAULT_LOCALE.getLanguage().toString();
	}

	public String getLanguages()
	{
		JSONObject ret = new JSONObject();

		try
		{
			Iterator<java.util.Map.Entry<String,Language>> i = mTranslationService.languages(mCredentials).entrySet().iterator();
			Integer j = 0;

			ret.put("known",new JSONObject());
			while(i.hasNext())
			{
				JSONObject o = i.next().getValue().toJSON();
				ret.put(o.getString("lc"),o);
				ret.getJSONObject("known").put(j.toString(),o.getString("lc"));

				j++;
			}

			return ret.toString();
		}
		catch(JSONException e)
		{
			return "{}";
		}
		catch(RuntimeException e)
		{
			return "{}";
		}
	}

	public String execute()
	{ 	
		try
		{
			super.execute();

			if(mHttpContext.getRequest().getParameter("page") != null)
			{
				Integer pid = new Integer(mHttpContext.getRequest().getParameter("page"));
				mPage = mPageManager.getPage(pid);

				if(mPage == null)
					addActionError("This page doesn't exists!");
			}
			else
				addActionError("This page doesn't exists!");
			
			return "success";
		}
		catch(RuntimeException e)
		{
			addActionError(e.getMessage());
			return "error";
		}
	}

	public String quote()
	{
		try
		{
			super.execute();

			return "success";
		}
		catch(RuntimeException e)
		{
			return "success";
		}
	}

	public String submit()
	{
		super.execute();

		if(mHttpContext.getRequest().getParameter("tgt-json") != null)
		{
			try
			{
				JSONObject tgts = new JSONObject(mHttpContext.getRequest().getParameter("tgt-json"));
				Iterator i = tgts.keys();

				while(i.hasNext())
				{
					String tgt = i.next().toString();
					Job j = new Job();
					Comment c = new Comment();

					c.setBody(mHttpContext.getRequest().getParameter("comment"));

					j.setBodySrc(mHttpContext.getRequest().getParameter("body"));
					j.setLcSrc(new Language(mHttpContext.getRequest().getParameter("src-lang")));
					j.setLcTgt(new Language(tgt));
					j.setTier(tgts.getJSONObject(tgt).getString("tier"));
					j.setSlug(mHttpContext.getRequest().getParameter("slug"));
					j.getThread().add(c);

					try
					{
						Job newj = mTranslationService.add(mCredentials,j,mHttpContext.getRequest().getParameter("auto-appr") != null);
						newj.setThread(mTranslationService.thread(mCredentials,newj));

						if(mDatabase.merge(newj))
							addActionMessage("New translation job #" + newj.getJobId().toString());
						else
							addActionMessage("Translation job #" + newj.getJobId().toString() + " was already ordered");
					}
					catch(RuntimeException e)
					{
						addActionError("Failed to order job");
					}
				}
			}
			catch(RuntimeException e)
			{
				addActionError("Failed to init page");
			}
			catch(JSONException e)
			{
				addActionError("Failed to parse target languages");
			}
		}

		return "success";
	}
}
