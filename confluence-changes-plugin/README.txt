A confluence plugin that exports a list of all changes to the confluence environment as a simple
RSS feed.

By simple we mean:

 * Programmatically simple - can read all details of the change with little parsing
 * Lightweight - only the change summary is sent, no diffs etc.
 
= Purpose =
The original purpose was to provide a way of determining what had changed within Confluence in the last X seconds.
This information is then used by our content aggregator to flush cached information.

= Configuration =
1. Acquire a copy of the Confluence binary distribution.
1. Copy ${CONFLUENCE_BINARY}/confluence/WEB-INF/classes/com to 
1. Copy ${CONFLUENCE_BINARY}/confluence/WEB-INF/lib/xwork-*.jar to 


