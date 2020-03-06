import javafx.application.Application; 
import javafx.scene.Scene; 
import javafx.scene.control.*; 
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class AddressBook extends Application {

	public static int currentRecord = 0;
	public static int totalRecord = 0;
	public static final int SIZE = (2 * (Character.BYTES * 32)) + (Character.BYTES * 20) + (Character.BYTES * 2) + (Character.BYTES * 5);
	public static RandomAccessFile file;
	private static String myName = null, myStreet = null, myCity = null, myState = null, myZip = null;

	public static void errorMessage() {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle("ERROR!");
	    alert.setContentText("Please enter valid data!");
	    alert.showAndWait();
	}

	public static void addSuccessMessage() {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle("SUCCESS!");
	    alert.setContentText("Record added Successfully!");
	    alert.showAndWait();
	}

	public static void amendSuccessMessage() {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle("SUCCESS!");
	    alert.setContentText("Record Updated Successfully!");
	    alert.showAndWait();
	}

	private static void writeString(RandomAccessFile file, String input, int len) throws IOException {
		StringBuffer buffer = null;
	    if (input != null)
	       buffer = new StringBuffer(input);
	    else
	       buffer = new StringBuffer(len);

	    buffer.setLength(len);
	    file.writeChars(buffer.toString());
	}
	public static void writeToFile(RandomAccessFile file) throws IOException {
		writeString(file, myName, 32);
		writeString(file, myStreet, 32);
		writeString(file, myCity, 20);
		writeString(file, myState, 2);
		writeString(file, myZip, 5);
		totalRecord++;
	}
	private static String readString(RandomAccessFile file, int len) throws IOException {
    	char[] s = new char[len];
    	for (int i = 0; i < s.length; i++)
    		s[i] = file.readChar();
    	return new String(s).replace('\0', ' ').trim();
    }
   	public static void readFromFile(RandomAccessFile file) throws IOException {
		myName = readString(file,32);
		myStreet = readString(file,32);
		myCity = readString(file,20);
		myState = readString(file,2);
		myZip = readString(file,5);
	}

	@Override public void start(Stage primaryStage) throws IOException {

		file = new RandomAccessFile("Address.dat", "rw"); 
		totalRecord = (int)file.length() / SIZE;
		currentRecord = totalRecord + 1;


		primaryStage.setTitle("Address Book");
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		Label name = new Label("Name");
		grid.add(name,1,1);
		TextField txtName = new TextField();
		grid.add(txtName,2,1,5,1);

		Label street = new Label("Street");
		grid.add(street,1,2);
		TextField txtStreet = new TextField();
		grid.add(txtStreet,2,2,5,1);

		Label city = new Label("City");
		grid.add(city,1,3);
		TextField txtCity = new TextField();
		grid.add(txtCity,2,3);

		Label state = new Label("State");
		grid.add(state,3,3);
		TextField txtState = new TextField();
		grid.add(txtState,4,3);

		Label zip = new Label("Zip");
		grid.add(zip,5,3);
		TextField txtZip = new TextField();
		grid.add(txtZip,6,3);

		GridPane gridButtons = new GridPane();
		gridButtons.setHgap(50);

		Button add = new Button("Add");
		gridButtons.add(add,0,0);
		add.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        if(!txtName.getText().trim().isEmpty() && !txtStreet.getText().trim().isEmpty() && !txtCity.getText().trim().isEmpty() && !txtState.getText().trim().isEmpty() && !txtZip.getText().trim().isEmpty())
		        {
		        	myName = txtName.getText();
			        myStreet = txtStreet.getText();
			        myCity = txtCity.getText();
			        myState  = txtState.getText();
			        myZip = txtZip.getText();

			        if(!(myName.trim().length() <= 32 && myStreet.trim().length() <= 32 && myCity.trim().length() <= 20 && myState.trim().length() <= 2 && myZip.trim().length() <= 5)) {
			        	errorMessage();
			        }
			        else {
				        try {
				        	file.seek(totalRecord*SIZE);
				        	writeToFile(file);
				        	currentRecord = totalRecord+1;
				        	txtName.setText("");
							txtStreet.setText("");
							txtCity.setText("");
							txtState.setText("");
							txtZip.setText("");
				        	addSuccessMessage();
				        }
				        catch(IOException ex) {
				        	System.out.println("OPPS! Something went wrong!");
				        	System.exit(0);
				        }
			        }
		        }
		        else
		        	errorMessage();
		    }
		});

		Button first = new Button("First");
		gridButtons.add(first,1,0);

		first.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        if(totalRecord>0)
		        {
		        	try {
			        	file.seek(0);
						txtName.setText(readString(file,32));
						txtStreet.setText(readString(file,32));
						txtCity.setText(readString(file,20));
						txtState.setText(readString(file,2));
						txtZip.setText(readString(file,5));
						currentRecord = 1;
					}
					catch(IOException ex) {
						System.out.println("Opps! Something went wrong!");
						System.exit(0);
					}
		        }
		    }
		});

		Button next = new Button("Next");
		gridButtons.add(next,2,0);
		next.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	if(txtName.getText().trim().isEmpty())
		    		currentRecord = 0;
		        if(totalRecord>0 && currentRecord<totalRecord)
		        {
		        	try {
		        		currentRecord++;
			        	file.seek((currentRecord-1)*SIZE);
						txtName.setText(readString(file,32));
						txtStreet.setText(readString(file,32));
						txtCity.setText(readString(file,20));
						txtState.setText(readString(file,2));
						txtZip.setText(readString(file,5));
					}
					catch(IOException ex) {
						System.out.println("Opps! Something went wrong!");
						System.exit(0);
					}
		        }
		    }
		});

		Button previous = new Button("Previous");
		gridButtons.add(previous,3,0);
		previous.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        if(totalRecord>0 && currentRecord>1)
		        {
		        	try {
		        		currentRecord--;
			        	file.seek((currentRecord-1)*SIZE);
						txtName.setText(readString(file,32));
						txtStreet.setText(readString(file,32));
						txtCity.setText(readString(file,20));
						txtState.setText(readString(file,2));
						txtZip.setText(readString(file,5));
					}
					catch(IOException ex) {
						System.out.println("Opps! Something went wrong!");
						System.exit(0);
					}
		        }
		    }
		});

		Button last = new Button("Last");
		gridButtons.add(last,4,0);
		last.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        if(totalRecord>0)
		        {	
		        	try {
		        		totalRecord = (int)file.length()/SIZE;
			        	file.seek((totalRecord-1)*SIZE);
						txtName.setText(readString(file,32));
						txtStreet.setText(readString(file,32));
						txtCity.setText(readString(file,20));
						txtState.setText(readString(file,2));
						txtZip.setText(readString(file,5));
						currentRecord = totalRecord;
					}
					catch(IOException ex) {
						System.out.println("Opps! Something went wrong!" + ex.toString());
						System.exit(0);
					}
		        }
		    }
		});

		Button update = new Button("Update");
		gridButtons.add(update,5,0);
		update.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        if(!txtName.getText().trim().isEmpty() && !txtStreet.getText().trim().isEmpty() && !txtCity.getText().trim().isEmpty() && !txtState.getText().trim().isEmpty() && !txtZip.getText().trim().isEmpty())
		        {
		        	myName = txtName.getText();
			        myStreet = txtStreet.getText();
			        myCity = txtCity.getText();
			        myState  = txtState.getText();
			        myZip = txtZip.getText();

			        if(!(myName.trim().length() <= 32 && myStreet.trim().length() <= 32 && myCity.trim().length() <= 20 && myState.trim().length() <= 2 && myZip.trim().length() <= 5)) {
			        	errorMessage();
			        }
			        else {
				        try {
				        	file.seek((currentRecord-1)*SIZE);
				        	writeToFile(file);
							totalRecord = (int)file.length() / SIZE;
							currentRecord = totalRecord + 1;
							txtName.setText("");
							txtStreet.setText("");
							txtCity.setText("");
							txtState.setText("");
							txtZip.setText("");
				        	amendSuccessMessage();
				        }
				        catch(IOException ex) {
				        	System.out.println("OPPS! Something went wrong!");
				        	System.exit(0);
				        }
			        }
		        }
		        else
		        	errorMessage();
		    }
		});


		grid.add(gridButtons,2,4,5,1);

		Scene scene = new Scene(grid, 600, 150);	
		primaryStage.setScene(scene); 
		primaryStage.show();

	}

}