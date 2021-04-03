package me.rcj0003.clans.config;

public enum MessageType {
	NO_ARGUMENTS("no-args"),
	NO_PERMISSION("no-perms"),
	COMMAND_NOT_FOUND("command-dne"),
	REQUIRES_PLAYER("player-required"),
	INVALID_ARGUMENT("invalid-arg"),
	USAGE("usage"),
	JOIN_GAME("join-game"),
	LEAVE_GAME("leave-game"),
	INVITE_ANNOUNCEMENT("invite-announce"),
	INVITE_RECEIVED("invite-received"),
	REVOKE_INVITE("revoke-invite"),
	KICK_ANNOUNCEMENT("kick-announcement"),
	LEAVE_CLAN("leave-clan"),
	TRANSFER_LEADERSHIP("transfer-leader"),
	PROMOTION_ERROR("promotion-error"),
	IN_CLAN_ERROR("in-clan-error"),
	NOT_IN_CLAN_ERROR("not-in-clan-error"),
	TOGGLE_CHAT_ON("toggle-chat-on"),
	TOGGLE_CHAT_OFF("toggle.chat-off"),
	CLAN_CHAT("clan-chat"),
	CREATE_CLAN("create"),
	DISBAND_CLAN("disband"),
	DISBAND_CLAN_CONFIRM("disband-confirm"),
	MEMBER_NOT_IN_CLAN("member-not-in-clan"),
	HIGHER_ROLE_REQUIRED("higher-role-required"),
	LOADING("loading"),
	LEADERBOARD_HEADER("leaderboard-header"),
	LEADERBOARD_FORMAT("leaderboard-format"),
	TRANSFER_REQUIRED("transfer-required"),
	CLICK_TO_ACCEPT("click-to-accept"),
	INVITE_SENDER("invite-sender"),
	NOT_INVITED("not-invited"),
	INVITE_LIST_HEADER("invite-list-header"),
	INVITE_LIST_FORMAT("invite-list-format"),
	INFO_HEADER("info-header"),
	INFO_MEMBER_FORMAT("info-member-format"),
	INFO_NAME_FORMAT("info-name-format"),
	INFO_STARS_FORMAT("info-stars-format"),
	INFO_MOTD_FORMAT("info-motd-format"),
	MOTD_NOT_UNLOCKED("motd-not-unlocked"),
	MOTD_TOO_LONG("motd-too-long"),
	TAG_NOT_UNLOCKED("tag-not-unlocked"),
	TAG_TOO_LONG("tag-too-long"),
	CAPACITY_SET("capacity-set"),
	GIVE_CURRENCY("give-currency"),
	AWARD_CURRENCY("award-currency"),
	JOIN_CLAN("join-clan"),
	DEMOTE("demote-announce"),
	PROMOTE("promote-announce"),
	INVITE("invite-announce"),
	PLAYER_NO_CLAN("player-no-clan"),
	CANT_DO_ON_SELF("cant-do-on-self"),
	UNKNOWN_CLAN_ERROR("unknown-clan-error"),
	CLAN_NAME_UPDATED("clan-name-updated"),
	CANT_PROMOTE("cant-promote"),
	CANT_DEMOTE("cant-demote");
	
	private String url;
	
	private MessageType(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
}