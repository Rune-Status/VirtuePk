package org.virtue.game.logic.content.dialogue;

import java.util.List;

import org.virtue.game.logic.node.entity.player.Player;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 15, 2014
 */
public abstract class Dialogue {
	
	public enum Options {
		FIRST, SECOND, THIRD, FOURTH, FIFTH;

		public static Options forComponent(int componentID) {
			for (Options opt : Options.values()) {
				if ((opt.ordinal() + 3) == componentID) {
					return opt;
				}
			}
			return null;
		}
	}
	
	public static int NO_EXPRESSION = 9760, SAD = 9764, SAD_TWO = 9768, NO_EXPRESSION_TWO = 9772, WHY = 9776;
	public static int SCARED = 9780, MIDLY_ANGRY = 9784, ANGRY = 9788, VERY_ANGRY = 9792, ANGRY_TWO = 9796;
	public static int MANIC_FACE = 9800, JUST_LISTEN = 9804, PLAIN_TALKING = 9808, LOOK_DOWN = 9812;
	public static int WHAT_THE = 9816, WHAT_THE_TWO = 9820, EYES_WIDE = 9824, CROOKED_HEAD = 9828;
	public static int GLANCE_DOWN = 9832, UNSURE = 9836, LISTEN_LAUGH = 9840, TALK_SWING = 9844, NORMAL = 9847;
	public static int GOOFY_LAUGH = 9851, NORMAL_STILL = 9855, THINKING_STILL = 9859, LOOKING_UP = 9862;

	protected Player player;
	private List<Object> parameters;
	protected int stage;
	
	public abstract int npcId();

	public abstract void onStart();

	public abstract void process(Options option);
	
	public void sendPlayerDialogue(int animationId, String... lines) {
		StringBuffer buffer = new StringBuffer();
		for (int curIdx = 0; curIdx < lines.length; curIdx++) {
			lines[curIdx] = lines[curIdx].replaceAll("@name@", player.getAccount().getUsername().getAccountName());
			buffer.append(" " + lines[curIdx]);
		}
		
	}

	

}
