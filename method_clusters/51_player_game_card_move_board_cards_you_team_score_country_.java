public static void main(String[] args)
	{
		GamePlayer p = new HumanBreakthroughPlayer("HUMAN");	// must have this name
		p.compete(args);
	}
--------------------

public static void train() {
		if(p.training) {
			if(es) {
				say("This is the training hall. Here, you can specialize by" +
						"\ntraining under different instructors. You can train once per town," +
						"\nat which point you have learned all you can.");
				say("There are two kinds of training you can take:" +
						"\nBarbarian: You hit harder and have more HP." +
						"\nWarrior: You hit harder and crit more often.");
				in = JOptionPane.showInputDialog("What training do you want to take? Warrior/Barbarian");
			} else {
				in = ask("Which training?");
				if(in.equals("b"))
					in = "barbarian";
				else if(in.equals("w"))
					in = "warrior";
			}
			if(in.equalsIgnoreCase("warrior")) {
				p.abilities[0]++;
				p.abilities[1]++;
				p.training = false;
			} else if(in.equalsIgnoreCase("barbarian")) {
				p.abilities[0]++;
				p.abilities[2]++;
				p.training = false;
			} else {
				say( "You searched and searched for someone who could teach" +
						"\nyou to be a "+in+" but couldn't find one.");
			}
			if(!p.training && es) {
				say( "Training...");
				say( "Done!");
			}
		} else {
			say("You have already learned all you could from"
					+ "\nthe instructors in this town. Move on to learn more.");
		}
		calcTotals();
	}
--------------------

public Action beatThis(Action move) {
		if (move == Action.ROCK) return Action.PAPER;
		else if (move == Action.PAPER) return Action.SCISSORS;
		else return Action.ROCK;
	}
--------------------

