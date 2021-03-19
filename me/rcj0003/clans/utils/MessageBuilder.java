package me.rcj0003.clans.utils;

import java.util.Collection;

import me.rcj0003.clans.utils.Utils.StringUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class MessageBuilder {
	private TextComponent textComponent;
	
	public MessageBuilder() {
		textComponent = new TextComponent("");
	}
	
	public MessageBuilder(TextComponent... components) {
		textComponent = new TextComponent(components);
	}
	
	public MessageBuilder addEvent(HoverEvent event) {
		textComponent.setHoverEvent(event);
		return this;
	}
	
	public MessageBuilder addEvent(ClickEvent event) {
		textComponent.setClickEvent(event);
		return this;
	}
	
	public MessageBuilder addText(String... text) {
		addText(true, text);
		return this;
	}
	
	public MessageBuilder addText(boolean lineBreak, String... text) {
		text = StringUtils.convertColorCodes(text);
		
		for (String line : text) {
			for (BaseComponent component : TextComponent.fromLegacyText(line))
				textComponent.addExtra(component);
			if (lineBreak)
				lineBreak();
		}
		
		return this;
	}
	
	public MessageBuilder addText(Collection<String> text) {
		addText(text, true);
		return this;
	}
	
	public MessageBuilder addText(Collection<String> text, boolean lineBreak) {
		for (String line : text) {
			line = StringUtils.convertColorCodes(line);
			for (BaseComponent component : TextComponent.fromLegacyText(line))
				textComponent.addExtra(component);
			if (lineBreak)
				lineBreak();
		}
		
		return this;
	}
	
	public MessageBuilder lineBreak() {
		textComponent.addExtra(new TextComponent(ComponentSerializer.parse("{text: \"\n\"}")));
		return this;
	}
	
	public BaseComponent build() {
		return textComponent;
	}
}