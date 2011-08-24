package com.mygengo.confluence;

import java.util.*;

public interface TranslationService
{
	/**
	 * Get all supported source languages.
	 * @param cred Credentials used to auth request.
	 */
	public Map<String,Language> languages(Credentials cred);

	/**
	 * Get current account balance.
	 * @param cred Credentials used to auth request.
	 */
	public String balance(Credentials cred);

	/**
	 * Send new job for translation.
	 * @param cred Credentials used to auth request.
	 * @param j New job. Only the source/target languages and body must be populated.
	 * @param auto_appr True if job should be auto approved (w/o user interaction).
	 * @return new job to be merged.
	 */
	public Job add(Credentials cred,Job j,Boolean auto_appr);

	/**
	 * Fetches all jobs from the remote server - exessive polling will get you banned USE W/ CAUTION!
	 * @param cred Credentials used to auth request.
	 * @return List of fully populated job list w/ comment thread.
	 */
	public List<Job> list(Credentials cred);

	/**
	 * Gets a single job from remote.
	 * @param cred Credentials used to auth request.
	 * @param j Job to fetch. Only jobId is used.
	 * @return Fully populated job object.
	 */
	public Job job(Credentials cred,Job j);

	/**
	 * Fetches the comment thread of a given job.
	 * @param cred Credentials used to auth request.
	 * @param j Job to work on. Only jobId is used.
	 * @return Thread of this comment.
	 */
	public List<Comment> thread(Credentials cred,Job j);

	/**
	 * Cancels a job in the 'available' state.
	 * @param cred Credentials used to auth request.
	 * @param j Job to cancel. Only jobId is used.
	 */
	public void cancel(Credentials cred,Job j);

	/**
	 * Get the feedback that was submitted earlier.
	 * @param cred Credentials used to auth request.
	 * @param j Job to work on. Only jobId is used.
	 */
	public Feedback feedback(Credentials cred,Job j);

	/**
	 * Approve a translation and send a feedback to myGengo and the translator.
	 * @param cred Credentials used to auth request.
	 * @param j Job. Only jobId is used.
	 */
	public void approve(Credentials cred,Job j,String rating,String for_trans,String for_mygengo,Boolean is_public);

	/**
	 * Construct a url to get a preview gif of the job.
	 * @param cred Credentials used to auth request.
	 * @param j Job. Only jobId is used.
	 * @return url
	 */
	public String preview(Credentials cred,Job j);

	/**
	 * Post a comment into the thread of a running translation job.
	 * @param cred Credentials used to auth request.
	 * @param j Job - only jobId is used.
	 * @param c Comment to post. Only body is used.
	 * @return new comment
	 */
	public Comment comment(Credentials cred,Job j,Comment c);

	/**
	 * Finally reject a translation.
	 * @param cred Credentials used to auth request.
	 * @param j Job to reject.
	 * @param follow_up Next action (see apidoc)
	 * @param reason Why the job was rejected (see apidoc)
	 * @param comment Optional text for myGengo.
	 * @param captcha Solved captcha.
	 */
	public void reject(Credentials cred,Job j,String follow_up,String reason,String comment,String captcha);

	/**
	 * Request a correction of a translation before final approval.
	 * @param cred Credentials used to auth request.
	 * @param j Job.
	 * @param c Detailed correction request.
	 */
	public void correct(Credentials cred,Job j,String c);

	/**
	 * Get a quote for a array of jobs. The function sets the "Credits" attribute in the supplied jobs.
	 * @param cerd Credentials used to auth the request.
	 * @param js Vector of jobs with source body, tier, target and source language set.
	 */
	public void quote(Credentials cred,List<Job> js);
}
