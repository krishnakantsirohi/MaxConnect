import java.util.*;

/**
 * This is the AiPlayer class.  It simulates a minimax player for the max
 * connect four game.
 * The constructor essentially does nothing. 
 *
 * @author james spargo
 *
 */

public class AiPlayer
{
    /**
     * The constructor essentially does nothing except instantiate an
     * AiPlayer object.
     *
     */
    public AiPlayer()
    {
        // nothing to do here
    }


    /**
     * This method plays a piece randomly on the board
     * @param currentGame The GameBoard object that is currently being used to
     * play the game.
     * @return an integer indicating which column the AiPlayer would like
     * to play in.
     */
    public int findBestPlay( GameBoard currentGame, int depth )
    {
        int action = -1;
        if (currentGame.getCurrentTurn()==1){
            int vsl = Integer.MAX_VALUE;
            for (int i=0;i<7;i++){
                if (currentGame.isValidPlay(i)){
                    GameBoard nextState = new GameBoard(currentGame.getGameBoard());
                    nextState.playPiece(i);
                    int temp = MaxValue(nextState,Integer.MIN_VALUE,Integer.MAX_VALUE,depth);
                    if (vsl>temp){
                        action = i;
                        vsl = temp;
                    }
                }
            }
        }
        else {
            int val = Integer.MIN_VALUE;
            for (int i=0;i<7;i++){
                if (currentGame.isValidPlay(i)) {
                    GameBoard nextState = new GameBoard(currentGame.getGameBoard());
                    nextState.playPiece(i);
                    int temp = MinValue(nextState,Integer.MIN_VALUE,Integer.MAX_VALUE,depth);
                    if (val<temp){
                        action = i;
                        val = temp;
                    }
                }
            }
        }
        return action;
    }

    private int MaxValue(GameBoard state, int alpha, int beta, int depth){
        if (!state.isTerminalState() && depth>0){
            int maxVal = Integer.MIN_VALUE;
            for (int i=0;i<7;i++){
                if (state.isValidPlay(i)){
                    GameBoard nextState = new GameBoard(state.getGameBoard());
                    nextState.playPiece(i);
                    int val = MinValue(nextState,alpha,beta,depth-1);
                    if (maxVal<val) maxVal=val;
                    if (maxVal>=beta) return maxVal;
                    if (alpha<maxVal) alpha=maxVal;
                }
            }
            return maxVal;
        }
        else
            return state.utility(2)-state.utility(1);
    }

    private int MinValue(GameBoard state, int alpha, int beta, int depth){
        if (!state.isTerminalState() && depth>0){
            int minVal = Integer.MAX_VALUE;
            for (int i=0;i<7;i++){
                if (state.isValidPlay(i)){
                    GameBoard nextState= new GameBoard(state.getGameBoard());
                    nextState.playPiece(i);
                    int val = MaxValue(nextState,alpha,beta,depth-1);
                    if (minVal>val) minVal=val;
                    if (minVal<=alpha) return minVal;
                    if (beta>minVal) beta=minVal;
                }
            }
            return minVal;
        }
        else
            return state.utility(2)-state.utility(1);
    }
}