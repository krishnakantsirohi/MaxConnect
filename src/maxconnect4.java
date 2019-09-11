import java.io.*;
import java.util.Scanner;

/**
 *
 * @author James Spargo
 * This class controls the game play for the Max Connect-Four game. 
 * To compile the program, use the following command from the maxConnectFour directory:
 * javac *.java
 *
 * the usage to run the program is as follows:
 * ( again, from the maxConnectFour directory )
 *
 *  -- for interactive mode:
 * java MaxConnectFour interactive [ input_file ] [ computer-next / human-next ] [ search depth]
 *
 * -- for one move mode
 * java maxConnectFour.MaxConnectFour one-move [ input_file ] [ output_file ] [ search depth]
 *
 * description of arguments: 
 *  [ input_file ]
 *  -- the path and filename of the input file for the game
 *
 *  [ computer-next / human-next ]
 *  -- the entity to make the next move. either computer or human. can be abbreviated to either C or H. This is only used in interactive mode
 *
 *  [ output_file ]
 *  -- the path and filename of the output file for the game.  this is only used in one-move mode
 *
 *  [ search depth ]
 *  -- the depth of the minimax search algorithm
 *
 *
 */

public class maxconnect4
{
    static AiPlayer calculon;
    public static void main(String[] args)
    {
        // check for the correct number of arguments
        if( args.length != 4 )
        {
            System.out.println("Four command-line arguments are needed:\n"
                    + "Usage: java [program name] interactive [input_file] [computer-next / human-next] [depth]\n"
                    + " or:  java [program name] one-move [input_file] [output_file] [depth]\n");

            exit_function( 0 );
        }

        // parse the input arguments
        String game_mode = args[0].toString();				// the game mode
        String input = args[1].toString();					// the input game file
        int depthLevel = Integer.parseInt( args[3] );  		// the depth level of the ai search

        // create and initialize the game board
        GameBoard currentGame = new GameBoard( input );

        // create the Ai Player
        calculon = new AiPlayer();

        //  variables to keep up with the game
        int playColumn = 99;				//  the players choice of column to play
        boolean playMade = false;			//  set to true once a play has been made

        if( game_mode.equalsIgnoreCase( "interactive" ) )
        {
            new maxconnect4().interactivePlay(currentGame,args[2],depthLevel);
            //System.out.println("interactive mode is currently not implemented\n");
            return;
        }

        else if( game_mode.equalsIgnoreCase( "one-move" ) )
        {
            new maxconnect4().oneMove(currentGame,args[2],depthLevel);
            //System.out.println( "\n" + game_mode + " is an unrecognized game mode \n try again. \n" );
            return;
        }

        /////////////   one-move mode ///////////
        // get the output file name
        String output = args[2].toString();				// the output game file

        System.out.print("\nMaxConnect-4 game\n");
        System.out.print("game state before move:\n");

        //print the current game board
        currentGame.printGameBoard();
        // print the current scores
        System.out.println( "Score: Player 1 = " + currentGame.getScore( 1 ) +
                ", Player2 = " + currentGame.getScore( 2 ) + "\n " );

    } // end of main()

    /**
     * This method prompts human for the next move.
     * @param gameBoard The GameBoard object that is currently being used to
     * play the game.
     * @param nextPlayer The next valid player.
     * @param depth The level upto which the computer will search for its best move.
     */
    private void interactivePlay(GameBoard gameBoard, String nextPlayer, int depth){
        Player player1 = new Player(nextPlayer.equalsIgnoreCase("human-next")?"Human":"Computer",1);
        player1.score = gameBoard.getScore(player1.id);
        Player player2 = new Player(player1.name.equalsIgnoreCase("Human")?"Computer":"Human",2);
        player2.score = gameBoard.getScore(player2.id);
        boolean human = nextPlayer.equalsIgnoreCase("human-next")?true:false;
        gameBoard.printGameBoard();
        System.out.println(player1.name+": "+player1.score);
        System.out.println(player2.name+": "+player2.score);
        while (!gameBoard.isTerminalState()){
            int action = -1;
            if (human){
                human = false;
                action = humanAction(gameBoard);
                gameBoard.playPiece(action);
            }
            else {
                human = true;
                action = calculon.findBestPlay(gameBoard,depth);
                gameBoard.playPiece(action);
                gameBoard.printGameBoardToFile("computer.txt");
            }
            gameBoard.printGameBoard();
            System.out.println(player1.name+": "+gameBoard.getScore(player1.id));
            System.out.println(player2.name+": "+gameBoard.getScore(player2.id));
        }
    }

    /**
     * This method prompts human for the next move.
     * @param gameBoard The GameBoard object that is currently being used to
     * play the game.
     * @return an integer indicating which column the AiPlayer would like
     * to play in.
     */
    private int humanAction(GameBoard gameBoard){
        Scanner scanner = new Scanner(System.in);
        int  move = -1;
        System.out.println("Please enter a valid move to play:");
        move = scanner.nextInt();
        while (!gameBoard.isValidPlay(move)){
            move = scanner.nextInt();
            System.out.println("Invalid move!\n Please enter a valid move:");
        }
        return move;
    }

    /**
     * @param gameBoard The GameBoard object that is currently being used to
     * play the game.
     * @param outputFile The out will be written to this file.
     * @param depth The level upto which the computer will search for its best move.
     */
    private void oneMove(GameBoard gameBoard, String outputFile, int depth)
    {
        Player player = new Player("computer", gameBoard.currentTurn);
        gameBoard.printGameBoard();
        System.out.println("Player Score: "+gameBoard.getScore(player.id));
        int action = calculon.findBestPlay(gameBoard,depth);
        gameBoard.playPiece(action);
        gameBoard.printGameBoardToFile(outputFile);
        gameBoard.printGameBoard();
        System.out.println("Player Score: "+gameBoard.getScore(player.id));
    }

    /**
     * This method is used when to exit the program prematurly.
     * @param value an integer that is returned to the system when the program exits.
     */
    private static void exit_function( int value )
    {
        System.out.println("exiting from MaxConnectFour.java!\n\n");
        System.exit( value );
    }
} // end of class connectFour