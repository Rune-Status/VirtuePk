package org.virtue.game.logic.social.clans;

import org.virtue.game.logic.social.SocialUserAPI;
import org.virtue.game.logic.social.clans.internal.ClanPermission;

public interface ClanSettingsAPI {
	
	public ClanChannelAPI getChannelManager ();
	
	/**
	 * Returns whether the specified clan exists
	 * @param clanHash	The clan hash to check
	 * @return	True if the clan exists, false otherwise
	 */
	public boolean clanExists (long clanHash);
	
	public void registerPlayer (SocialUserAPI user);
	
	public void deregisterPlayer (SocialUserAPI user);
	
	/**
	 * Sends information about the clan member at the specified index to the player
	 * @param user		The player requesting the clan member info
	 * @param index		The index of the player who's information is being requested
	 * @param isGuest	Whether the player is requesting member info from a guest clan
	 */
	public void sendMemberInfo (SocialUserAPI user, int index, boolean isGuest);
	
	public void sendPermissionInfo (SocialUserAPI user, ClanRank group, boolean isGuest);
	
	/**
	 * Creates a new clan and places the data into the index
	 * @param name	The desired name of the clan
	 * @param owner The player who will become the clan owner
	 * @param founders	The players who will be initially recruited into the clan
	 * @return	true if the clan was created successfully, false if a clan already exists with the specified name
	 */
	public boolean createClan (String name, SocialUserAPI owner, SocialUserAPI... founders);
	
	/**
	 * Recruits a player into the recruiter's clan
	 * @param recruiter		The user performing the invite
	 * @param joiner		The user who is joining the clan
	 * @return				True if the user successfully joined the clan, false otherwise
	 */
	public boolean joinClan (SocialUserAPI recruiter, SocialUserAPI joiner);
	
	/**
	 * Removes a player from their current clan
	 * @param player	The player to remove
	 * @return			True if the player left the clan successfully, false otherwise
	 */
	public boolean leaveMyClan (SocialUserAPI player);
	
	/**
	 * Sets the rank of the specified player in the specified clan.
	 * Note that this method does allow server administrators (who would normally hold a rank of JMOD) to change the ranks of people in clans they don't belong to themselves.
	 * @param clanHash		The unique hash for the clan with the rank being edited
	 * @param player		The player performing the edit
	 * @param protocolName	The protocol displayname of the clan member who's rank is being changed
	 * @param rank			The rank to change to
	 * @return				True if the rank was changed successfully, false if there was an error.
	 */
	public boolean setRank (long clanHash, SocialUserAPI player, String protocolName, ClanRank rank);
	
	public boolean kickClanMember (long clanHash, SocialUserAPI player, String protocolName);
	
	public boolean setPermission (long clanHash, SocialUserAPI player, ClanRank group, ClanPermission permission, boolean holds);
}
