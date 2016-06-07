package main.java.main;

/*
import Message.*;
import Message.MessageRelated.*;
import Topics.*;
*/
import main.java.Users.*;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Created by servicedog on 7/8/15.
 */
public class SNSController {

    private String ENDPOINT;

    /**
     *
     * Constructor for the SNSController.
     *
     * @param end The endpoint. ex. http://tse.topicplaces.com/api/2/
     */
    public SNSController(String end)
    {
        ENDPOINT = end;
    }

    /**
     *
     * Checks that a connection can be made with the endpoint.
     * If not, program stalls until a connection can be reestablished.
     *
     */
    private void ensureConnection() {

        boolean connected = false;

        while ( !connected ) {
            try {
                URLConnection connection = new URL( ENDPOINT ).openConnection();
                connection.connect();

                connected = true;
                //System.out.println("Connection to " + ENDPOINT + " established.");
            } catch ( MalformedURLException e ) {
                throw new IllegalStateException("Bad URL: " + ENDPOINT, e);
            } catch ( IOException e ) {

                System.out.println( "Connection to " + ENDPOINT + " lost, attempting to reconnect..." );

                try {
                    Thread.sleep( 15000 );
                } catch ( InterruptedException s ) {
                    throw new IllegalStateException( "Sleep interrupted.");
                }
            }
        }
    }


    /**
     *
     * Uses an HTTP client to obtain an authentication key from the endpoint.
     *
     * @param username The username you'll be obtaining an authkey for.
     * @param password Password for the given username.
     * @return The authentication key for the endpoint.
     */
    public String acquireKey( String username, String password)
    {
        ensureConnection();

        RESTLogin logg = new RESTLogin( ENDPOINT );
        return logg.login( username, password );
    }

    /**
     *
     * Creates a new public topic.
     *
     * @param title The title/name of the new Public Topic
     * @param authkey The authentication key. See "acquireKey()"
     * @return The ID code of the newly created topic (in format "t-[id]")

    public String newPublicTopic(String title, String authkey)
    {
        ensureConnection();

        TopicCreator tc = new TopicCreator( ENDPOINT );
        String topicID = tc.createTopic(title, false, authkey);

        if ( topicID.equals( "" ) )
        {
            System.err.println( "Unable to create new topic" );
            return "";
        }

        return topicID;
    }

    /**
     *
     * Creates a new private topic.
     *
     * @param title The title/name of the new Private Topic
     * @param authkey The authentication key. See "acquireKey()"
     * @return The ID code of the newly created topic (in format "grp-[id]")

    public String newPrivateTopic( String title, String authkey )
    {
        ensureConnection();

        TopicCreator tc = new TopicCreator( ENDPOINT );
        String topicID = tc.createTopic( title, true, authkey );

        if ( topicID.equals( "" ) )
        {
            System.err.println( "Unable to create new topic" );
            return "";
        }

        return topicID;
    }

    /**
     *
     * Updates an existing topic.
     *
     * @param title The updated title of the topic. If null, title remains unchanged.
     * @param desc The updated description of the topic. If null, description remains unchanged.
     * @param media The updated media of the topic. If null, media remains unchanged.
     * @param isPrivate True if the topic is private. False if public.
     * @param TID The ID of the topic to be updated.
     * @param authkey The authentication key. See "acquireKey."

    public void updateTopic( String title, String desc, String media, Boolean isPrivate, String TID, String authkey ) {

        ensureConnection();
        TopicUpdater tupd = new TopicUpdater( ENDPOINT );
        tupd.updateTopic(title, desc, media, isPrivate, TID, authkey);
    }

    /**
     *
     * Posts a new message to a specific public topic.
     *
     * @param title The title/name of the message.
     * @param desc The description of the message.
     * @param mediaID The media (in format "m-[id]") of the message. See "newMediaFromLocal" or "newMediaFromURL."
     * @param topicID The ID code of the private topic (in format "t-[id]")
     * @param authkey The authentication key. See "acquireKey."
     * @return The ID code of the newly created message (in format "g-[id]")

    public String newPublicMessage(String title, String desc, String mediaID, String topicID, String authkey)
    {
        ensureConnection();

        MessagePoster tp = new MessagePoster( ENDPOINT );

        return tp.execute(title, desc, mediaID, authkey, topicID);
    }

    /**
     *
     * Posts a new message to a specific private topic.
     *
     * @param title The title/name of the message.
     * @param desc The description of the message.
     * @param mediaID The media (in format "m-[id]")of the message. See "newMediaFromLocal" or "newMediaFromURL."
     * @param topicID The ID code of the private topic (in format "grp-[id]")
     * @param authkey The authentication key. See "acquireKey."
     * @return The ID code of the newly created message (in format "g-[id]")

    public String newPrivateMessage(String title, String desc, String mediaID, String topicID, String authkey)
    {
        ensureConnection();

        PrivateMessagePoster tp = new PrivateMessagePoster( ENDPOINT );
        String pM = tp.execute(title, desc, mediaID, authkey, topicID);

        return pM;
    }

    /**
     *
     * Updates an existing message.
     *
     * @param mess The content/title of the message. If null, message remains unchanged.
     * @param desc The description of the message. If null, description remains unchanged.
     * @param mediaID The media (in format "m-[id]") of the message. If null, media remains unchanged.
     * @param GID The message ID (in format "g-[id]").
     * @param authkey The authentication key. See "acquireKey"

    public void updateMessage(String mess, String desc, String mediaID, String GID, String authkey) {
        ensureConnection();
        MessageUpdater gupd = new MessageUpdater( ENDPOINT );
        gupd.execute(mess, desc, mediaID, GID, authkey);
    }

    /**
     *
     * Returns the contents of a given message in JSON format.
     *
     * @param GID The ID of the message (in format "g-[id]")
     * @param isPrivate True if the given message is private. False if public.
     * @param authkey The authentication key. See "acquireKey"
     * @return The JSON string of a given message.

    public String getMessageJSON(String GID, boolean isPrivate, String authkey) {
        ensureConnection();

        MessageRetriever gret = new MessageRetriever( ENDPOINT );
        return gret.getMessageJSON(GID, isPrivate, authkey);
    }

    /**
     *
     * Obtains the media ID from a given message.
     *
     * @param GID The ID of the message (in format "g-[id]")
     * @param isPrivate True if the message is private. False if public.
     * @param authkey The authentication key. See "acquireKey"
     * @return The media ID (in format "m-[id]") from the supplied message ID.

    public String getMessageMedia(String GID, boolean isPrivate, String authkey) {
        ensureConnection();

        String MID = null;
        if ( isPrivate ) {
            MID = new MediaRetriever( ENDPOINT ).getPrivateGameMedia( GID, authkey );
        } else {
            MID = new MediaRetriever( ENDPOINT ).getGameMedia( GID , authkey );
        }

        if ( MID.equals( "" ) ) {
            MID = null;
        }

        return MID;
    }

    /**
     *
     * Obtains the title/content of a given message.
     *
     * @param GID The ID of the message (in format "g-[id]")
     * @param isPrivate True if the message is private. False if public.
     * @param authkey The authentication key. See "acquireKey"
     * @return The title from the supplied message ID.

    public String getMessageTitle(String GID, boolean isPrivate, String authkey) {
        ensureConnection();
        MessageRetriever gret = new MessageRetriever( ENDPOINT );
        return gret.getTitleFromJSON(gret.getMessageJSON(GID, isPrivate, authkey));
    }

    /**
     *
     * Obtains the description of a given message.
     *
     * @param GID The ID of the message (in format "g-[id]")
     * @param isPrivate True if the message is private. False if public.
     * @param authkey The authentication key. See "acquireKey"
     * @return The description from the supplied message ID.

    public String getMessageDescription(String GID, boolean isPrivate, String authkey) {
        ensureConnection();
        MessageRetriever gret = new MessageRetriever( ENDPOINT );
        return gret.getDescriptionFromJSON(gret.getMessageJSON(GID, isPrivate, authkey));
    }

    /**
     *
     * Returns the contents of a given topic in JSON format.
     *
     * @param TID The ID of the topic (in format "t-[id]")
     * @param isPrivate True if the given topic is private. False if public.
     * @param authkey The authentication key. See "acquireKey"
     * @return The JSON string of a given Topic

    public String getTopicJSON(String TID, boolean isPrivate, String authkey) {
        ensureConnection();
        TopicRetriever tRet = new TopicRetriever( ENDPOINT );
        return tRet.getTopicInfoJSON(TID, isPrivate, authkey);
    }

    /**
     *
     * Obtains the media ID from a given topic.
     *
     * @param TID The ID of the topic (in format "t-[id]")
     * @param isPrivate True if the topic is private. False if public.
     * @param authkey The authentication key. See "acquireKey"
     * @return The media ID (in format "m-[id]") from the supplied topic ID.

    public String getTopicMedia(String TID, boolean isPrivate, String authkey) {
        ensureConnection();

        String MID = new MediaRetriever( ENDPOINT ).getTopicMedia( TID, isPrivate, authkey );

        if ( MID.equals( "" ) ) {
            MID = null;
        }

        return MID;
    }

    /**
     *
     * Obtains the title/content of a given topic.
     *
     * @param TID The ID of the topic (in format "g-[id]")
     * @param isPrivate True if the topic is private. False if public.
     * @param authkey The authentication key. See "acquireKey"
     * @return The title from the supplied topic ID.

    public String getTopicTitle(String TID, boolean isPrivate, String authkey) {
        ensureConnection();
        TopicRetriever tRet = new TopicRetriever( ENDPOINT );
        return tRet.getTitleFromJSON(tRet.getTopicInfoJSON( TID, isPrivate, authkey));
    }

    /**
     *
     * Obtains the description of a given topic.
     *
     * @param TID The ID of the topic (in format "g-[id]")
     * @param isPrivate True if the topic is private. False if public.
     * @param authkey The authentication key. See "acquireKey"
     * @return The description from the supplied topic ID.

    public String getTopicDescription(String TID, boolean isPrivate, String authkey) {
        ensureConnection();
        TopicRetriever tRet = new TopicRetriever( ENDPOINT );
        return tRet.getDescriptionFromJSON(tRet.getTopicInfoJSON(TID, isPrivate, authkey));
    }

    /**
     *
     * Adds a link to a message.
     *
     * @param URL The URL for the link
     * @param GID The message to add the link to
     * @param authkey The authentication key. See "acquireKey"

    public void newLink( String URL, String GID, String authkey ) {

        LinkMaker lMak = new LinkMaker( ENDPOINT );
        lMak.createLinkForGame( URL, GID, authkey );
    }

    /**
     *
     * Invites a user to a private topic.
     *
     * @param UID The ID of the user to invite (in format "u-[id]")
     * @param PID The private topic to invite the user to (in format "grp-[id]")
     * @param authkey The authentication key. See "acquireKey"
     * @return The invitation ID ("i-[id]")

    public String invite(String UID, String PID, String authkey) {
        ensureConnection();

        UserInviter ui = new UserInviter( ENDPOINT );
        return ui.inviteUserToPID(UID, PID, authkey);
    }

    /**
     *
     * Makes one user follow another, returns a subscription ID.
     *
     * @param followerUID The ID of the follower.
     * @param followeeUID The ID of the followee.
     * @param authkey The authentication key. See "acquireKey"
     * @return The subscription ID ("sub-[id]")

    public String follow(String followerUID, String followeeUID, String authkey ) {

        SubscriptionCreator screat = new SubscriptionCreator( ENDPOINT );
        return screat.execute(followerUID, followeeUID, authkey);

    }

    /**
     *
     * Adds attributes (keys and corresponding values) to a message.
     *
     * @param attributes The attributes to add. A map of the attributes and their values.
     * @param GID The message ID (in format "g-[id]") to add attributes to.
     * @param authkey The authentication key. See "acquireKey"

    public void newMessageAttributes(Map<String, String> attributes, String GID, String authkey)
    {
        ensureConnection();

        MessageUpdater gupd = new MessageUpdater( ENDPOINT );
        gupd.addAttributes(attributes, GID, authkey);
    }

    /**
     * Retrieves a list of keys and corresponding values posted to a given message.
     *
     * @param GID The message ID (in format "g-[id]") to retrieve attributes from.
     * @param isPrivate True if the message is private. False if public.
     * @param authkey The authentication key. See "acquireKey"
     * @return A Map containing the keys and corresponding values posted to a message.

    public Map<String,String> getAttributes( String GID, Boolean isPrivate, String authkey ) {
        ensureConnection();

        AttributeListRetriever alr = new AttributeListRetriever( ENDPOINT );

        return alr.getList(GID, isPrivate, authkey);
    }

    /**
     *
     * Adds an option to a message.
     *
     * @param text The option name/content.
     * @param gameID The message ID (in format "g-[id]") to add an option to.
     * @param authkey The authentication key. See "acquireKey"
     * @return The ID of the created option.

    public String newMessageOption(String text, String gameID, String authkey)
    {
        ensureConnection();

        OptionMaker oMak = new OptionMaker( ENDPOINT );
        String optionID = oMak.createOptionForGame(text, gameID, authkey);

        if ( optionID.equals( "" ) )
        {
            //System.err.println( "Unable to create new option" );
            return "";
        }

        return optionID;
    }

    /**
     *
     * Creates a new user.
     *
     * @param name User's actual name.
     * @param username User's desired username.
     * @param email User's email.
     * @param password User's desired password.
     * @return The ID of the new user (in format "u-[id]").

    public String newUser(String name, String username, String email, String password)
    {

        ensureConnection();
        UserCreator uCreat = new UserCreator( ENDPOINT );

        return uCreat.createUser(name, username, email, password);

    }

    /**
     *
     * Verifies that the email belongs to a valid user at the endpoint.
     *
     * @param email The email to verify.
     * @return The ID of the user (in format "u-[id]"), or the empty string ("") if the user doesn't exist.

    public String verifyEmail(String email)
    {
        ensureConnection();

        return verifyUsername(email);
    }

    /**
     *
     * Verifies that the username belongs to a valid user at the endpoint.
     *
     * @param user The username to verify.
     * @return The ID of the user (in format "u-[id]"), or the empty string ("") if the user doesn't exist.

    public String verifyUsername(String user)
    {
        ensureConnection();
        UserRetriever uGett = new UserRetriever( ENDPOINT );

        return uGett.getUserFromIDorEmail(user);
    }

    /**
     *
     * Gets the username associated with a valid user at the endpoint.
     *
     * @param user The user ID to retrieve the username for.
     * @return The ID of the user (in format "u-[id]"), or the empty string ("") if the user doesn't exist.

    public String getUsernameFromID(String user)
    {
        ensureConnection();
        UserRetriever uGett = new UserRetriever( ENDPOINT );

        return uGett.getUsernameFromID(user);
    }

    /**
     *
     * Retrieves all of a user's private topics.
     *
     * @param userID The user ID (in format "u-[id]") to retrieve private topics from.
     * @return A list of all of the user's private topics and their associated IDs (in format "grp-[id]")

    public Map<String, String> getPrivateTopicMap(String userID) {
        ensureConnection();
        PrivateTopicsListRetriever ptlr = new PrivateTopicsListRetriever( ENDPOINT );

        return ptlr.getList(userID);
    }

    /**
     *
     * Retrieves all of a user's public topics.
     *
     * @param userID The user ID (in format "u-[id]") to retrieve public topics from.
     * @return A list of all of the user's public topics and their associated IDs (in format "t-[id]")

    public Map<String, String> getPublicTopicMap(String userID) {
        ensureConnection();
        TopicsListRetriever ptlr = new TopicsListRetriever( ENDPOINT );

        return ptlr.getList(userID);
    }

    /**
     *
     * Retrieves a map of private messages to their corresponding message IDs.
     *
     * @param TID The private topic ID (in format "grp-[id]") to obtain messages from.
     * @param authkey The authentication key. See "acquireKey"
     * @return A map of the topic's private messages to their corresponding message IDs.

    public Map<String, String> getPrivateMessageMap(String TID, String authkey) {
        ensureConnection();
        MessageListRetriever glr = new MessageListRetriever( ENDPOINT );

        Map<String, String> lis = glr.getMap(TID, true, authkey);


        return lis;
    }

    /**
     *
     * Retrieves a list of private message IDs in a given topic.
     *
     * @param TID The private topic ID (in format "grp-[id]") to obtain messages from.
     * @param authkey The authentication key. See "acquireKey"
     * @return A map of the topic's private messages to their corresponding message IDs.

    public List<String> getPrivateMessageGIDList(String TID, String authkey) {
        ensureConnection();
        MessageListRetriever glr = new MessageListRetriever( ENDPOINT );

        List<String> lis = glr.getGIDList( TID, true, authkey);

        return lis;
    }

    /**
     *
     * Retrieves a map of public messages to their corresponding message IDs.
     *
     * @param TID The public topic ID (in format "t-[id]") to obtain messages from.
     * @param authkey The authentication key. See "acquireKey"
     * @return A map of the topic's public messages to their corresponding message IDs.

    public Map<String, String> getPublicMessageMap(String TID, String authkey) {
        ensureConnection();
        MessageListRetriever glr = new MessageListRetriever( ENDPOINT );

        Map<String,String> list = glr.getMap(TID, false, authkey);
        return list;
    }

    /**
     *
     * Gets a map of followers to their corresponding subscription IDs
     *
     * @param ID Identifier of the topic, message, or user to retrieve followers for.
     * @return A map of followers to their corresponding subscription IDs.

    public Map<String, String> getFollowerSubMap(String ID) {
        ensureConnection();

        FollowerListRetriever flr = new FollowerListRetriever( ENDPOINT );
        return flr.getUserSubscriptionMap(ID);
    }

    /**
     *
     * Gets a map of followers mapped to their user IDs
     *
     * @param ID Identifier of the topic, message, or user to retrieve followers for.
     * @return A map of followers to their corresponding user IDs.

    public Map<String, String> getFollowerIDMap(String ID) {
        ensureConnection();

        FollowerListRetriever flr = new FollowerListRetriever( ENDPOINT );
        Map<String, String> map = flr.getUserIDMap(ID);
        Map<String, String> nmap = new HashMap<String, String>();

        Iterator<String> iter = map.keySet().iterator();
        while( iter.hasNext() ) {
            String next = iter.next();
            nmap.put( getUsernameFromID( next ), next );
        }

        return nmap;
    }

    /**
     *
     * Retrives options and their IDs from a given message.
     *
     * @param GID The ID of the message to retrieve options from.
     * @param isPrivate True if the message is private. False if public.
     * @param authkey The authentication key. See "acquireKey."
     * @return A map of options to their IDs for a given message.

    public Map<String, String> getOptionsIDMap(String GID, Boolean isPrivate, String authkey) {
        ensureConnection();
        OptionRetriever optRet = new OptionRetriever( ENDPOINT );
        return optRet.getIDMap(GID, isPrivate, authkey);
    }

    /**
     *
     * Retrives options and their answer counts from a given message.
     *
     * @param GID The ID of the message to retrieve options from.
     * @param isPrivate True if the message is private. False if public.
     * @param authkey The authentication key. See "acquireKey."
     * @return A map of options to the number of responders.

    public Map<String, String> getOptionsAnswerMap(String GID, Boolean isPrivate, String authkey) {
        ensureConnection();
        OptionRetriever optRet = new OptionRetriever( ENDPOINT );
        return optRet.getAnswerMap(GID, isPrivate, authkey);
    }

    /**
     *
     * Deletes a topic.
     *
     * @param id The ID of the topic to delete.
     * @param isPrivate True if the topic is private. False if public.
     * @param authkey The authentication key. See "acquireKey"

    public void deleteTopic( String id, Boolean isPrivate, String authkey ) {
        ensureConnection();
        TopicDeleter tdel = new TopicDeleter( ENDPOINT );
        tdel.execute(id, isPrivate, authkey);
    }

    /**
     *
     * Deletes a user.
     *
     * @param id The ID of the user to delete (in format "u-[id]" )
     * @param authkey The authentication key. See "acquireKey." The authentication key
     *                must be obtained using the user-to-be-deleted's credentials.

    public void deleteUser( String id , String authkey ) {
        ensureConnection();
        UserDeleter udel = new UserDeleter( ENDPOINT );
        udel.execute( id, authkey );
    }

    /**
     *
     * Deletes a message.
     *
     * @param GID The ID of the message to delete (in format "g-[id]" )
     * @param authkey The authentication key. See "acquireKey."

    public void deleteMessage(String GID, String authkey) {
        ensureConnection();
        MessageDeleter gd = new MessageDeleter( ENDPOINT );
        gd.execute( GID, authkey );
    }

    /**
     *
     * Uploads an image from your local directory to a message.
     *
     * @param f The file to upload from your local directory
     * @param authkey The authentication key. See "acquireKey."
     * @return The media ID of the uploaded image.

    public String newMediaFromLocal(File f, String authkey) {
        ensureConnection();
        MediaUploader mUp = new MediaUploader( ENDPOINT );
        return mUp.uploadFromLocal( f, authkey);
    }

    /**
     *
     * Uploads an image from an online URL.
     *
     * @param URL The url location of the image.
     * @param authkey The authentication key. See "acquireKey."
     * @return The media ID of the uploaded image.

    public String newMediaFromURL(String URL, String authkey) {
        ensureConnection();
        MediaUploader mUp = new MediaUploader( ENDPOINT );
        return mUp.uploadFromURL(URL, authkey);
    }

    /**
     *
     * Deletes a subscription.
     *
     * @param subID The ID of the subscription to delete (in format "sub-[id]" )
     * @param authkey The authentication key. See "acquireKey"

    public void deleteSubscription( String subID, String authkey ) {
        ensureConnection();

        SubscriptionDeleter sd = new SubscriptionDeleter( ENDPOINT );
        sd.execute( subID, authkey );
    }


    /**
     *
     * Creates a new message using another message as a template.
     *
     * @param name The name of the new message. Ignored if sameName is true.
     * @param sameName True if message name will be the same as the template message.
     * @param fromGID The template message ID
     * @param toTID The topic to post the message to.
     * @param isPrivate True if private, false if public.
     * @param authkey The authentication key. See "acquireKey"
     * @return The new message ID

    public String newMessageFromTemplate(String name, Boolean sameName, String fromGID, String toTID, boolean isPrivate, String authkey) {

        ensureConnection();

        MessageRetriever messRet = new MessageRetriever( ENDPOINT );
        OptionRetriever oRet = new OptionRetriever( ENDPOINT );
        AttributeListRetriever alr = new AttributeListRetriever( ENDPOINT );
        OptionMaker optMak = new OptionMaker( ENDPOINT );
        MessageUpdater mUpd = new MessageUpdater( ENDPOINT );

        String json = messRet.getMessageJSON( fromGID, isPrivate, authkey);

        String title = null;
        if ( sameName ) {
            title = messRet.getTitleFromJSON(json);
        } else {
            title = name;
        }

        String desc = messRet.getDescriptionFromJSON(json);
        String media = messRet.getMediaFromJSON(json);

        Map<String, String> optMap = oRet.getIDMap(fromGID, isPrivate, authkey);
        Map<String, String> attsMap = alr.getList(fromGID, isPrivate, authkey);

        String newGame = null;

        if ( isPrivate ) {
            newGame = newPrivateMessage(title, desc, media, toTID, authkey);
        } else {
            newGame = newPublicMessage(title, desc, media, toTID, authkey);
        }

        mUpd.addAttributes(attsMap, newGame, authkey);

        ArrayList<String> newList = new ArrayList<String>();
        Iterator<String> iter = optMap.keySet().iterator();
        while ( iter.hasNext() ) {
            newList.add(iter.next());
        }
        Collections.sort(newList);

        iter = newList.iterator();
        while( iter.hasNext() ) {
            optMak.createOptionForGame( iter.next(), newGame, authkey );
        }

        return newGame;
    }

    /**
     *
     * Copies all of the messages (including their attributes and options) from
     * a template topic and posts them to another topic.
     *
     * @param name The new topic's name. Ignored if sameName is true.
     * @param sameName True if the new topic will have the same name as the template
     * @param fromTID The template topic's ID.
     * @param isPrivate True if the template topic is private. False if public.
     * @param authkey The authentication key. See "acquireKey."
     * @return The new topic ID

    public String newTopicFromTemplate(String name, Boolean sameName, String fromTID, boolean isPrivate, String authkey) {

        ensureConnection();

        String title = null;
        if ( sameName ) {
            title = getTopicTitle( fromTID, isPrivate, authkey );
        } else {
            title = name;
        }

        String mediaID = getTopicMedia( fromTID, isPrivate, authkey );

        String TID = null;
        if ( isPrivate ) {
            TID = newPrivateTopic( title, authkey );
        } else {
            TID = newPublicTopic( title, authkey );
        }

        if ( mediaID != null ) {
            updateTopic( null, null, mediaID, isPrivate, TID, authkey );
        }

        MessageListRetriever glr = new MessageListRetriever( ENDPOINT );
        Map<String, String> messageList = glr.getMap(fromTID, isPrivate, authkey);
        Iterator<String> it = messageList.keySet().iterator();

        while ( it.hasNext() ) {

            String messageName = it.next();
            String messageGID = messageList.get(messageName);

            System.out.println("Creating " + messageName + "...");

            newMessageFromTemplate( null, true, messageGID, TID, isPrivate, authkey);
        }

        return TID;
    }
*/
}
