package com.mygengo.confluence;

import java.util.*;
import com.atlassian.confluence.json.parser.*;

/**
 * Form for approving jobs.
 */
public class ReviewAction extends MyGengoActionSupport
{
	public String getPreviewURL()
	{
		return mTranslationService.preview(mCredentials,mJob);
	}

	public String execute()
	{ 	
		try
		{
			super.execute();
			return "success";
		}
		catch(RuntimeException e)
		{
			addActionError(e.getMessage());
			return "error";
		}
	}

	public String submit()
	{
		super.execute();

		try
		{
			mTranslationService.approve(mCredentials,mJob,
				mHttpContext.getRequest().getParameter("rating"),
				mHttpContext.getRequest().getParameter("for_trans"),
				mHttpContext.getRequest().getParameter("for_mygengo"),
				mHttpContext.getRequest().getParameter("public") != null);

			addActionMessage("Job approved");

			mJob = mTranslationService.job(mCredentials,mJob);
			System.out.println(mJob.getStatus() + " " + mJob.getBodyTgt());
			mDatabase.merge(mJob);
			return "success";
		}
		catch(RuntimeException e)
		{
			addActionError(e.getMessage());
			return "error";
		}
	}
}
