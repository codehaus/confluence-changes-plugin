package org.codehaus.confluence.changes;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.atlassian.confluence.core.ContentEntityManager;
import com.atlassian.confluence.core.ContentEntityObject;
import com.atlassian.confluence.pages.BlogPost;
import com.atlassian.confluence.pages.Comment;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.rpc.SecureRpc;
import com.atlassian.confluence.security.Permission;
import com.atlassian.confluence.security.PermissionManager;
import com.atlassian.confluence.user.PersonalInformation;
import com.opensymphony.user.User;

public class ChangesRPCImpl implements ChangesRPC, SecureRpc
{
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
        return com.atlassian.confluence.user.AuthenticatedUserThreadLocal.getUser();
    }

    private ContentEntityManager contentEntityManager;
    private PermissionManager permissionManager;

    public List getChanges( String token, String lastChecked ) throws Exception
    {
        try
        {
            Date lastCheckedDate = new Date( Long.parseLong( lastChecked ) );

            List<Map<String, Object>> results = new Vector<Map<String, Object>>();
            List<ContentEntityObject> entitiesModifiedSince = contentEntityManager
                            .getEntitiesModifiedSince( lastCheckedDate );

            for ( ContentEntityObject ce : entitiesModifiedSince )
            {
                if ( !permissionManager.hasPermission( getUser(), Permission.VIEW, ce ) )
                {
                    continue;
                }

                Map<String, Object> map = new Hashtable<String, Object>();
                results.add( map );

                map.put( "id", convertLong( ce.getId() ) );
                map.put( "lastModificationDate", convertDate( ce.getLastModificationDate() ) );
                map.put( "lastModifierName", convertString( ce.getLastModifierName() ) );
                map.put( "realTitle", convertString( ce.getRealTitle() ) );

                if ( ce instanceof PersonalInformation )
                {
                    PersonalInformation o = (PersonalInformation) ce;
                    map.put( "type", "PersonalInformation" );
                    continue;
                }

                if ( ce instanceof Page )
                {
                    Page page = (Page) ce;
                    map.put( "type", "Page" );
                    continue;
                }

                if ( ce instanceof Comment )
                {
                    Comment comment = (Comment) ce;
                    map.put( "type", "Comment" );
                    continue;
                }

                if ( ce instanceof BlogPost )
                {
                    BlogPost blogPost = (BlogPost) ce;
                    map.put( "type", "BlogPost" );
                    continue;
                }

                //Fall back            
                map.put( "className", ce.getClass().getName() );
                results.add( map );
            }
            return results;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw e;
        }
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
}
