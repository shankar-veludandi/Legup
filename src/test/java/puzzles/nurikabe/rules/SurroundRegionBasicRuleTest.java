package puzzles.nurikabe.rules;

import legup.MockGameBoardFacade;
import legup.TestUtilities;
import model.tree.TreeNode;
import model.tree.TreeTransition;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import puzzle.nurikabe.Nurikabe;
import puzzle.nurikabe.NurikabeBoard;
import puzzle.nurikabe.NurikabeCell;
import puzzle.nurikabe.NurikabeType;
import puzzle.nurikabe.rules.SurroundRegionBasicRule;
import save.InvalidFileFormatException;

public class SurroundRegionBasicRuleTest
{

    private static final SurroundRegionBasicRule RULE = new SurroundRegionBasicRule();
    private static Nurikabe nurikabe;

    @BeforeClass
    public static void setUp()
    {
        MockGameBoardFacade.getInstance();
        nurikabe = new Nurikabe();
    }

    @Test
    public void SurroundRegionBasicRule_SurroundRegionBlackTest() throws InvalidFileFormatException
    {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/SurroundRegionBasicRule/SurroundRegionBlack", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        NurikabeCell cell1 = board.getCell(1,0);
        cell1.setValueInt(NurikabeType.BLACK.toValue());
        board.addModifiedData(cell1);
        NurikabeCell cell2 = board.getCell(0,1);
        cell2.setValueInt(NurikabeType.BLACK.toValue());
        board.addModifiedData(cell2);
        NurikabeCell cell3 = board.getCell(2,1);
        cell3.setValueInt(NurikabeType.BLACK.toValue());
        board.addModifiedData(cell3);
        NurikabeCell cell4 = board.getCell(1,2);
        cell4.setValueInt(NurikabeType.BLACK.toValue());
        board.addModifiedData(cell4);

        Assert.assertNull(RULE.checkRule(transition));

        for(int i = 0; i < transition.getBoard().getElementCount(); i++)
        {
            if(i == cell1.getIndex() || i == cell2.getIndex() || i == cell3.getIndex() || i == cell4.getIndex())
            {
                Assert.assertNull(RULE.checkRuleAt(transition, i));
            }
            else
            {
                Assert.assertNotNull(RULE.checkRuleAt(transition, i));
            }
        }
    }

    @Test
    public void SurroundRegionBasicRule_SurroundRegionBlackInCornerTest() throws InvalidFileFormatException
    {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/SurroundRegionBasicRule/SurroundRegionBlackInCorner", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        NurikabeCell cell1 = board.getCell(1,0);
        cell1.setValueInt(NurikabeType.BLACK.toValue());
        board.addModifiedData(cell1);
        NurikabeCell cell2 = board.getCell(0,1);
        cell2.setValueInt(NurikabeType.BLACK.toValue());
        board.addModifiedData(cell2);

        Assert.assertNull(RULE.checkRule(transition));

        for(int i = 0; i < transition.getBoard().getElementCount(); i++)
        {
            if(i == cell1.getIndex() || i == cell2.getIndex())
            {
                Assert.assertNull(RULE.checkRuleAt(transition, i));
            }
            else
            {
                Assert.assertNotNull(RULE.checkRuleAt(transition, i));
            }
        }
    }
}
