package edu.rpi.legup.puzzle.shorttruthtable;

import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

import java.awt.*;

import java.util.List;
import java.util.ArrayList;

class ShortTruthTableImporter extends PuzzleImporter{

    public ShortTruthTableImporter(ShortTruthTable stt) {
        super(stt);
    }




    private List<ShortTruthTableCell> getCells(String statement, int rowIndex){
        List<ShortTruthTableCell> cells = new ArrayList<ShortTruthTableCell>();
        for(int i = 0; i<statement.length(); i++){
            ShortTruthTableCell cell = new ShortTruthTableCell(statement.charAt(i), ShortTruthTableCellType.UNKNOWN, new Point(i, rowIndex));
            cell.setModifiable(true);
            cells.add(cell);
        }
        return cells;
    }




    //STATEMENT IMPORTER

    /**
     * Creates the board for building
     *
     * @param node xml document node
     * @throws InvalidFileFormatException
     */
    @Override
    public void initializeBoard(Node node) throws InvalidFileFormatException {

        try {

            //Check File formatting
            if (!node.getNodeName().equalsIgnoreCase("board")) {
                throw new InvalidFileFormatException("short truth table Importer: cannot find board puzzleElement");
            }
            Element boardElement = (Element) node;
            if (boardElement.getElementsByTagName("data").getLength() == 0) {
                throw new InvalidFileFormatException("short truth table Importer: no statements found for board");
            }


            //get all the cells in a 2D arraylist
            List<List<ShortTruthTableCell>> allCells = new ArrayList<List<ShortTruthTableCell>>();
            //store the statement data structors
            List<ShortTruthTableStatement> statements = new ArrayList<ShortTruthTableStatement>();
            //store the max length
            int maxStatementLength = 0;

            //get the elements from the file
            Element dataElement = (Element) boardElement.getElementsByTagName("data").item(0);
            NodeList statementData = dataElement.getElementsByTagName("statement");
            NodeList cellData = dataElement.getElementsByTagName("cell");

            //get a 2D arraylist of all the given cells
            for (int i = 0; i < statementData.getLength(); i++) {

                //Get the atributes from the statement i in the file
                NamedNodeMap attributeList = statementData.item(i).getAttributes();
                String statementRep = attributeList.getNamedItem("representation").getNodeValue();
                int rowIndex = Integer.valueOf(attributeList.getNamedItem("row_index").getNodeValue());

                //get the cells for the statement
                List<ShortTruthTableCell> rowOfCells = getCells(statementRep, rowIndex);
                allCells.add(rowOfCells);
                statements.add(new ShortTruthTableStatement(statementRep, rowOfCells));

                //keep track of the length of the longest statement
                maxStatementLength = Math.max(maxStatementLength, statementRep.length());

            }

            //go through the given cells
            for(int i = 0; i<cellData.getLength(); i++){

                //get the atributes for the cell
                NamedNodeMap attributeList = cellData.item(i).getAttributes();
                int rowIndex = Integer.valueOf(attributeList.getNamedItem("row_index").getNodeValue());
                int charIndex = Integer.valueOf(attributeList.getNamedItem("char_index").getNodeValue());
                String cellType = attributeList.getNamedItem("type").getNodeValue();

                //modify the appropriet cell
                ShortTruthTableCell  cell = allCells.get(rowIndex).get(charIndex);
                cell.setData(ShortTruthTableCellType.valueOf(cellType));
                cell.setModifiable(false);
                cell.setGiven(true);

            }


            //calculate the width and height for the board
            int width = maxStatementLength;
            int height = statements.size();

            System.out.println("Board dimentions "+width+", "+height);

            //instantiate the board with the correct width and height
            ShortTruthTableBoard sttBoard = new ShortTruthTableBoard(width, height, statements);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    ShortTruthTableCell cell = null;
                    if(x < statements.get(y).getLength()) {
                        cell = allCells.get(y).get(x);
                        System.out.println(cell);
                    }else{
                        System.out.println("making empty cell");
                        cell = new ShortTruthTableCell(' ', ShortTruthTableCellType.NOT_IN_PLAY, new Point(x, y));
                        cell.setModifiable(false);
                    }
                    cell.setIndex(y * height + x);
                    sttBoard.setCell(x, y, cell);
                }
            }


            System.out.println("\n\n\n\n");
            puzzle.setCurrentBoard(sttBoard);

        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException("short truth table Importer: unknown value where integer expected");
        }


    }




}