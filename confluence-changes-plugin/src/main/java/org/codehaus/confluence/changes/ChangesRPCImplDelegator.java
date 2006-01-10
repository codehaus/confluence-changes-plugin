package org.codehaus.confluence.changes;

import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.atlassian.confluence.core.ContentEntityManager;
import com.atlassian.confluence.core.ContentEntityObject;
import com.atlassian.confluence.rpc.SecureRpc;
import com.atlassian.confluence.security.Permission;
import com.atlassian.confluence.security.PermissionManager;
import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.user.User;

public class ChangesRPCImplDelegator implements ChangesRPC, SecureRpc
{

    private ContentEntityManager contentEntityManager;

    private PermissionManager permissionManager;

    public String login( String username, String password )
    {
        //No implementation required; intercepted by Confluence
        return null;
    }

    public boolean logout( String token )
    {
        //No implementation required; intercepted by Confluence
        return false;
    }

    private User getUser()
    {
        return AuthenticatedUserThreadLocal.getUser();
    }

    public String getSystemTime( String token )
    {
        return Long.toString( System.currentTimeMillis() );
    }

    public List getChanges( String token, String lastChecked ) throws Exception
    {
        try
        {
            Date lastCheckedDate = new Date( Long.parseLong( lastChecked ) );

            List results = new Vector();
            List entitiesModifiedSince = contentEntityManager.getEntitiesModifiedSince( lastCheckedDate );

            for ( Iterator iter = entitiesModifiedSince.iterator(); iter.hasNext(); )
            {
                ContentEntityObject ce = ( ContentEntityObject ) iter.next();
                System.out.println( "Examining " + ce );
                try
                {

                    if ( !hasPermission( ce ) )
                    {
                        continue;
                    }

                    final Map map = new Hashtable();
                    results.add( map );

                    map.put( "id", convertLong( ce.getId() ) );
                    map.put( "lastModificationDate", convertDate( ce.getLastModificationDate() ) );
                    map.put( "lastModifierName", convertString( ce.getLastModifierName() ) );
                    map.put( "realTitle", convertString( ce.getRealTitle() ) );
                    map.put( "type", getType( ce ) );

                    results.add( map );
                }
                catch ( Exception e )
                {
                    System.out.println( "Error processing " + ce + " : " + e.getLocalizedMessage() );
                }

            }
            return results;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw e;
        }
    }

    private String getType( ContentEntityObject ce )
    {
        String className = ce.getClass().getName();
        int lastDot = className.lastIndexOf( '.' ) + 1;
        return className.substring( lastDot );
    }

    private Object convertString( String s )
    {
        return s == null ? "" : s;
    }

    private String convertLong( long id )
    {
        return Long.toString( id );
    }

    private String convertDate( Date date )
    {
        if ( date == null )
        {
            return "";
        }

        return Long.toString( date.getTime() );
    }

    public void setContentEntityManager( ContentEntityManager contentEntityManager )
    {
        this.contentEntityManager = contentEntityManager;
    }

    public void setPermissionManager( PermissionManager permissionManager )
    {
        this.permissionManager = permissionManager;
    }

    public boolean hasPermission( ContentEntityObject ce )
    {
        try
        {
            return permissionManager.hasPermission( getUser(), Permission.VIEW, ce );
        }
        catch ( Error e )
        {
            //Oh well, no soup for you.
            System.out.println( "Failure getting permission: " + e.getLocalizedMessage() + " for " + ce );
            return false;
        }
    }

}
