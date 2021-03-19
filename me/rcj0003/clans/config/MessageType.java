package me.rcj0003.clans.config;

public enum MessageType {
	NO_ARGUMENTS("no-args"),
	NO_PERMISSION("no-perms"),
	COMMAND_NOT_FOUND("command-dne"),
	REQUIRES_PLAYER("player-required"),
	INVALID_ARGUMENT("invalid-arg"),
	USAGE("usage"),
	PLAYER_REQUIRED("commands.player-required"),
	JOIN_GAME("join-game"),
	LEAVE_GAME("leave-game"),
	INVITE_ANNOUNCEMENT("commands.invite-announce"),
	INVITE_RECEIVED("commands.invite-received"),
	REVOKE_INVITE("commands.revoke-invite"),
	KICKED_PARTY("commands.kick-recipient"),
	KICK_ANNOUNCEMENT("commands.kick-announcement"),
	LEAVE_CLAN("commands.leave-clan"),
	TOGGLE_CHAT("commands.toggle-chat"),
	TRANSFER_LEADERSHIP("commands.transfer-leader"),
	PROMOTION_ERROR("commands.promotion-error"),
	IN_CLAN_ERROR("commands.in-clan-error"),
	NOT_IN_CLAN_ERROR("commands.not-in-clan-error"),
	TOGGLE_CHAT_ON("commands.toggle-chat-on"),
	TOGGLE_CHAT_OFF("commands.toggle.chat-off"),
	CLAN_CHAT("clan-chat"),
	CREATE_CLAN("create"),
	DISBAND_CLAN("disband"),
	MEMBER_NOT_IN_CLAN("member-not-in-clan"),
	HIGHER_ROLE_REQUIRED("higher-role-required");
	
	private String url;
	
	private MessageType(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return "messages." + url;
	}
}