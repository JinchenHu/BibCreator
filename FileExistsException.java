import java.io.IOException;

public class FileExistsException extends IOException{
	public FileExistsException() {
		super("Exception: There is already an existing file for that author. File will be renamed as BU, and Older BU files will be deleted!");	
	}
	public FileExistsException(String s) {
		super(s);
	}
}
