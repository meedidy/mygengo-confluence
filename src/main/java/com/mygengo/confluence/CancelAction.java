package com.mygengo.confluence;

/**
 * Cancel a job in the 'avaliable' state.
 */
public class CancelAction extends MyGengoActionSupport
{
	public String execute()
	{ 
		try
		{
			if(super.execute() == "success")
			{
				mTranslationService.cancel(mCredentials,mJob);
				mJob = mTranslationService.job(mCredentials,mJob);
				mDatabase.merge(mJob);

				addActionMessage("Job #" + mJob.getJobId() + " canceled");
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
}
