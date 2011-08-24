package com.mygengo.confluence;

import java.util.List;

/**
 * Manage jobs and comment thread in the local database.
 */
public interface Database
{
	/**
	 * Write the job into the database, updates the
	 * set if one with the same job_id exists.
	 *
	 * @return true if the job already existed, false if a new set was inserted.
	 */
	public Boolean merge(Job j);

	/**
	 * Removes the job from the database.
	 *
	 * @return true if job was remove, false if no job with this
	 * job_id existed.
	 */
	public Boolean remove(Job j);

	/**
	 * Deletes all jobs in the local database
	 *
	 * @return # of deleted jobs
	 */
	public Integer purge();

	/**
	 * Write the credentials into the database
	 */
	public void merge(Credentials cred);

	/**
	 * Get all saved jobs
	 */
	public List<Job> all();

	/**
	 * Get the saved credentials, if none are saved return empty ones.
	 */
	public Credentials credentials();

	/**
	 * Get a job by its job_id
	 */
	public Job job(Integer jid);
}
