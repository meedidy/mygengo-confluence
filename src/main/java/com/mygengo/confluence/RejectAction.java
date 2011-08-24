package com.mygengo.confluence;

import java.util.*;
import com.atlassian.confluence.json.parser.*;

/**
 * Form for rejecting jobs.
 */
public class RejectAction extends MyGengoActionSupport
{
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
		String ret = "error";

		super.execute();

		if(mHttpContext.getRequest().getParameter("comment") == null || 
			 mHttpContext.getRequest().getParameter("comment").length() == 0)
		{
			addActionError("Please add a comment.");
			return "error";
		}

		try
		{
			mTranslationService.reject(mCredentials,mJob,
				mHttpContext.getRequest().getParameter("follow_up"),
				mHttpContext.getRequest().getParameter("reason"),
				mHttpContext.getRequest().getParameter("comment"),
				mHttpContext.getRequest().getParameter("captcha"));

			addActionMessage("Rejection submitted");

			ret = "success";
		}
		catch(RuntimeException e)
		{
			addActionError("Failed to submit. Typed the CAPTCHA wrong?");
			ret = "error";
		}
		finally
		{
			try
			{
				mJob = mTranslationService.job(mCredentials,mJob);
				mDatabase.merge(mJob);
			}
			catch(RuntimeException e)
			{
				addActionError(e.getMessage());
			}

			return ret;
		}
	}
}
