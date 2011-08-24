package com.mygengo.confluence;

import java.util.*;
import com.atlassian.confluence.json.parser.*;

/**
 * Form for correcting jobs.
 */
public class CorrectAction extends MyGengoActionSupport
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
		super.execute();

		try
		{
			mTranslationService.correct(mCredentials,mJob,mHttpContext.getRequest().getParameter("comment"));
			addActionMessage("Request submitted");

			mJob.setStatus("revising");
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
