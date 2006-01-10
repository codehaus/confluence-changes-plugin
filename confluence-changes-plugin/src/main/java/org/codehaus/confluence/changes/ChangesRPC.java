package org.codehaus.confluence.changes;

import java.util.List;

public interface ChangesRPC
{

    List getChanges( String token, String lastChecked ) throws Exception;

    String getSystemTime( String token );

}
