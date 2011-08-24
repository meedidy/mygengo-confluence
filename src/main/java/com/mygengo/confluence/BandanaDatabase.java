package com.mygengo.confluence;

import java.util.*;
import com.atlassian.bandana.*;
import com.atlassian.confluence.setup.bandana.KeyedBandanaContext;

/**
 * Implementation of the Job database using the Bandana service.
 */
public class BandanaDatabase implements com.mygengo.confluence.Database
{
	private class BandanaDatabaseCredentialsContext implements KeyedBandanaContext
	{
		public String getContextKey() { return "com.mygengo.confluence.context.credentials"; }
		public boolean hasParentContext() { return false; }
		public BandanaContext getParentContext() { return this; }
	}

	private class BandanaDatabaseJobContext implements KeyedBandanaContext
	{
		public String getContextKey() { return "com.mygengo.confluence.context.job"; }
		public boolean hasParentContext() { return false; }
		public BandanaContext getParentContext() { return this; }
	}

	private BandanaManager mBandanaManager;
	private BandanaContext mBandanaCredentialsContext;
	private BandanaContext mBandanaJobContext;
	private final String mCredentialsKey = "myGengoCredentials";

	public BandanaDatabase()
	{
		mBandanaCredentialsContext = new BandanaDatabaseCredentialsContext();
		mBandanaJobContext = new BandanaDatabaseJobContext();
	}

	public void setBandanaManager(BandanaManager b)
	{
		mBandanaManager = b;
	}

	public Boolean merge(Job j)
	{
		Boolean ret = (mBandanaManager.getValue(mBandanaJobContext,j.getJobId().toString()) == null);

		mBandanaManager.setValue(mBandanaJobContext,j.getJobId().toString(),j);
		return ret;
	}

	public Boolean remove(Job j) throws UnsupportedOperationException
	{
		Boolean ret = (mBandanaManager.getValue(mBandanaJobContext,j.getJobId().toString()) != null);

		if(ret)
			mBandanaManager.setValue(mBandanaJobContext,j.getJobId().toString(),null);
		return ret;
	}

	public Integer purge() throws UnsupportedOperationException
	{
		List<Job> l = all();
		Iterator<Job> i = l.iterator();

		while(i.hasNext())
			remove(i.next());

		return l.size();
	}

	public void merge(Credentials cred)
	{
		mBandanaManager.setValue(mBandanaCredentialsContext,mCredentialsKey,cred);		
		return;
	}

	public List<Job> all()
	{
		List<Job> ret = new LinkedList();
		Iterable<String> k = mBandanaManager.getKeys(mBandanaJobContext);
		Iterator<String> i = k.iterator();

		while(i.hasNext())
		{
			Job j = (Job)mBandanaManager.getValue(mBandanaJobContext,i.next());
			if(j != null)
				ret.add(j);
		}
		
		return ret;
	}

	public Credentials credentials()
	{
		Credentials cred = (Credentials)mBandanaManager.getValue(mBandanaCredentialsContext,mCredentialsKey);
		if(cred == null)
		{
			cred = new Credentials();
			cred.setApiKey("Enter API key");
			cred.setPrivKey("Enter Private key");
		}
		return cred;
	}

	public Job job(Integer jid) throws RuntimeException
	{
		Job ret = (Job)mBandanaManager.getValue(mBandanaJobContext,jid.toString());
		if(ret == null)
			throw new RuntimeException("No job with id " + jid.toString());
		else
			return ret;
	}
}
