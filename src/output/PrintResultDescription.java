package output;


public class PrintResultDescription extends PrintResults {

	/**
	 * Creates files of type "description" in path <code>descriptionFilePath</code>.
	 * @see PrintResults#PrintResults(String, String)
	 */
	public PrintResultDescription(final String descriptionFilePath){
		super(descriptionFilePath, "description");
	}
	
	public void println(final String line){
		this.getDescriptionFileWriter().println(line);
	}
	
	public void print(final String line){
		this.getDescriptionFileWriter().print(line + " ");
	}
}
