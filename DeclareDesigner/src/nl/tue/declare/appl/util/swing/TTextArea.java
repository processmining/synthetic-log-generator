package nl.tue.declare.appl.util.swing;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.plaf.metal.MetalBorders;
import javax.swing.text.Document;

public class TTextArea extends JTextArea{

	private static final long serialVersionUID = 354078395675120423L;
	
    /**
     * Constructs a new TextArea.  A default model is set, the initial string
     * is null, and rows/columns are set to 0.
     */
    public TTextArea() {
        this(null, null, 0, 0);
    }

    /**
     * Constructs a new TextArea with the specified text displayed.
     * A default model is created and rows/columns are set to 0.
     *
     * @param text the text to be displayed, or null
     */
    public TTextArea(String text) {
        this(null, text, 0, 0);
    }

    /**
     * Constructs a new empty TextArea with the specified number of
     * rows and columns.  A default model is created, and the initial
     * string is null.
     *
     * @param rows the number of rows >= 0
     * @param columns the number of columns >= 0
     * @exception IllegalArgumentException if the rows or columns
     *  arguments are negative.
     */
    public TTextArea(int rows, int columns) {
        this(null, null, rows, columns);
    }

    /**
     * Constructs a new TextArea with the specified text and number
     * of rows and columns.  A default model is created.
     *
     * @param text the text to be displayed, or null
     * @param rows the number of rows >= 0
     * @param columns the number of columns >= 0
     * @exception IllegalArgumentException if the rows or columns
     *  arguments are negative.
     */
    public TTextArea(String text, int rows, int columns) {
        this(null, text, rows, columns);
    }
	
    public TTextArea(Document doc) {
        this(doc, null, 0, 0);
    }
	
	public TTextArea(Document doc, String text, int rows, int columns){
		super(doc,text,rows,columns);
		setLineWrap(true);
		setWrapStyleWord(true);
	    Border in = new BasicBorders.MarginBorder();
	    Border out = new MetalBorders.TextFieldBorder();
	    setBorder(BorderFactory.createCompoundBorder(out,in));
	}

}
