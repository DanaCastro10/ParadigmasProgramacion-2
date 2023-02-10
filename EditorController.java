package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import model.ModelManager;
import model.Student;
import view.StudentEditor;

public class EditorController implements ActionListener {

	private Student student;
	private int currentId;
	
	private ModelManager model;
	private StudentEditor view;
	
	public EditorController(ModelManager model, StudentEditor view) {
		this.student = new Student();
		this.currentId = -1;
		
		this.model = model;
		this.view = view;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton)e.getSource();
		if (button == view.getBackButton()) {
			currentId--;
			goPrevious();
		} else if (button == view.getNextButton()) {
			currentId++;
			goNext();
		} else if (button == view.getUpdateButton()) {
			if (view.getStudentId().isEmpty()) {
				this.student = new Student();
				readForm();
				model.addStudent(student);
				goLast();
			} else {
				readForm();
				model.updateStudent(currentId, student);
			}
		}
	}
	
	private void goNext() {
		if (currentId == 0) {
			view.getBackButton().setEnabled(true);
		}
		try {
			student = model.getStudent(currentId);
			fillForm();
		} catch (IndexOutOfBoundsException ex) {
			view.getNextButton().setEnabled(false);
			currentId = -1;
			view.cleanForm();
		}
	}

	private void goPrevious() {
		if (currentId == -2) {
			try {
				student = model.getLastStudent();
				currentId = student.getId();
				fillForm();
			} catch (IndexOutOfBoundsException ex) {
				view.getBackButton().setEnabled(false);
				currentId = -1;
				view.cleanForm();
			}
			view.getNextButton().setEnabled(true);;
		} else if (currentId == -1) {
			view.getBackButton().setEnabled(false);
			view.cleanForm();
		} else {
			student = model.getStudent(currentId);
			fillForm();
		}
	}

	private void goLast() {
		student = model.getLastStudent();
		currentId = student.getId();
		fillForm();
		view.getBackButton().setEnabled(true);;
		view.getNextButton().setEnabled(true);;
	}

	private void fillForm() {
		view.setStudentId(String.valueOf(student.getId() + 1000));
		view.setStudentName(student.getName());
		view.setMajor(student.getMajor());
		view.setSemester(String.valueOf(student.getSemester()));
		view.setGrades(student.getGrades());
		view.getUpdateButton().setText("Actualizar estudiante");
	}
	
	private void readForm() {
		if (!view.getStudentId().isEmpty()) {
			student.setId(Integer.parseInt(view.getStudentId()) - 1000);
		}
		if (!view.getStudentName().isEmpty()) {
			student.setName(view.getStudentName());
		}
		if (!view.getMajor().isEmpty()) {
			student.setMajor(view.getMajor());
		}
		if (!view.getSemester().isEmpty()) {
			student.setSemester(Integer.parseInt(view.getSemester()));
		}
	}

}
