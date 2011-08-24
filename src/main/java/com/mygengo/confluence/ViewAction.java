package com.mygengo.confluence;

import com.atlassian.confluence.spaces.*;
import com.atlassian.confluence.pages.*;
import java.util.List;

/**
 * View a approved job and publish it as wiki page.
 */
public class ViewAction extends MyGengoActionSupport
{
	private Feedback mFeedback;
	private SpaceManager mSpaceManager;
	private PageManager mPageManager;

	public void setSpaceManager(SpaceManager s) { mSpaceManager = s; }	
	public void setPageManager(PageManager s) { mPageManager = s; }	
	
	public Feedback getFeedback() { return mFeedback; }
	public List<Space> getSpaces() { return mSpaceManager.getAllSpaces(); }

	public String execute()
	{ 
		try
		{
			if(super.execute() == "success")
			{
				mFeedback = mTranslationService.feedback(mCredentials,mJob);
				return "success";
			}
			else
			{
				addActionError("No job with supplied ID");
				return "error";
			}	
		}
		catch(RuntimeException e)
		{
			addActionError(e.toString() + " (" + e.getMessage() + ")");
			return "error";
		}
	}

	public String publish()
	{
		if(execute() == "success")
		{
			String space = mHttpContext.getRequest().getParameter("space");
			String title = mHttpContext.getRequest().getParameter("title");

			try
			{
				Space sp = mSpaceManager.getSpace(space);
				Page p = new Page();

				if(sp == null || title == null || title.length() == 0 || mPageManager.getPage(space,title) != null)
					throw new RuntimeException();
			
				p.setTitle(title);
				p.setContent(mJob.getBodyTgt());
				p.setSpace(sp);

				mPageManager.saveContentEntity(p,null);

				addActionMessage("New wiki page <a href='" + settingsManager.getGlobalSettings().getBaseUrl() + "/" + p.getUrlPath() + "'>" + p.getTitle() + "</a> created");
			}
			catch(RuntimeException e)
			{
				addActionError("Failed to create page '" + title + "' in Space '" + space + "'");
			}
		}
			
		return "error";
	}
}
