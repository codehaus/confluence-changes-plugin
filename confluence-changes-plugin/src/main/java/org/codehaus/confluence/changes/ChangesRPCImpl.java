package org.codehaus.confluence.changes;

import java.util.List;



public class ChangesRPCImpl
    implements ChangesRPC
{

    public ChangesRPCImpl()
    {
    }

    public void setChangesRPCImplDelegator(ChangesRPC changesrpc)
    {
        changesRPCImplDelegator = changesrpc;
    }

    public List getChanges(String s, String s1)
        throws Exception
    {
        return changesRPCImplDelegator.getChanges(s, s1);
    }

    public String getSystemTime(String s)
    {
        return changesRPCImplDelegator.getSystemTime(s);
    }

    private ChangesRPC changesRPCImplDelegator;
}


