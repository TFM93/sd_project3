/**
 * Rope game simulator
 *
 *Concurrent rope game simulation made for Distribucted Systems class, in order to explore concurrency in Java.
 * An aproximation to the classic game is assumed here.
 * A match is composed of three games and each game
 may take up to six trials. A game win is declared by asserting the position of a mark placed at the middle
 of the rope after six trials. The game may end sooner if the produced shift is greater or equal to four
 length units. We say in this case that the victory was won by knock out, otherwise, it will be a victory by
 points.
 A team has five elements, but only three compete at each trial. Member selection for the trial is carried
 out by the team's coach. He decides who will join for next trial according to some predefined strategy.
 Each contestant will loose one unit of strength when he is pulling the rope and will gain one unit when he
 is seating at the bench. Somehow the coach perceives the physical state of each team member and may
 use this information to substantiate his decision.
 In order to ensure rules compliance, there is a referee. She has full control of the procedure and
 decides when to start a new game or a trial within the game. She also decides when a game is over and
 declares who has won a game or the match.
 *<br>
 *
 * @author Ivo Silva (<a href="mailto:ivosilva@ua.pt">ivosilva@ua.pt</a>)
 * @author Tiago Magalhaes (<a href="mailto:tiagoferreiramagalhaes@ua.pt">tiagoferreiramagalhaes@ua.pt</a>)
 */


package pt.ua.sd.RopeGame;