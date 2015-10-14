package org.gradle.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class SwtDemo {
	public static void main(String[] args) {

		// ����Display,��Ӧ����ϵͳ�Ŀؼ���ʹ��������ͷ�
		Display display = new Display();

		// ����shell
		final Shell shell = new Shell(display);
		shell.setText("��һ��shell����");

		// ָ�������Ĳ�������
		GridLayout gl = new GridLayout(2, false);
		gl.marginBottom = 20;
		gl.marginTop = 10;
		gl.marginLeft = 30;
		gl.marginRight = 30;
		gl.verticalSpacing = 20;
		gl.horizontalSpacing = 20;
		shell.setLayout(gl);

		// ����������Ŀؼ�����ָ���ڷ�λ��
		Label label1 = new Label(shell, SWT.NONE);
		label1.setText("����");
		// ��һ�������ǲ���ϵͳ��Դ �ڶ�������������ʽ �����������ֵĸ߶ȣ��ֺŴ�С�� ��4������������ʾ��ʽ
		Font labelFont = new Font(display, "΢���ź�", 20, SWT.NONE);
		Text text1 = new Text(shell, SWT.BORDER);
		text1.setFont(labelFont);
		text1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		Label label2 = new Label(shell, SWT.NONE);
		label2.setText("����");
		Color foreColor = new Color(display, 255, 0, 0);
		label2.setForeground(foreColor);
		Text text2 = new Text(shell, SWT.BORDER | SWT.PASSWORD);
		text2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		Label label3 = new Label(shell, SWT.NONE);
		label3.setText("������");
		Combo combo = new Combo(shell, SWT.NONE);
		for (int i = 1; i <= 9; i++) {
			combo.add("ѡ��" + i);
		}
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		/**
		 * �������4�еı��
		 * 
		 * 1.������ͷ 2.������������
		 */
		// �����ʽ�Ƕ�ѡ ��ֱ ˮƽ����������ʾ��Ե
		Table table = new Table(shell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL
				| SWT.BORDER);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		table.setHeaderVisible(true);// ���ñ��ͷ�ɼ�
		table.setLinesVisible(true);// ���ñ���߿ɼ�

		// �������ͷ
		for (int i = 1; i <= 4; i++) {
			TableColumn tableColumn = new TableColumn(table, SWT.NONE);
			tableColumn.setText("��" + i + "��");
			tableColumn.setWidth(200);
		}
		// ������������
		TableItem item1 = new TableItem(table, SWT.NONE);
		item1.setText(new String[] { "111", "222", "3333", "444" });

		// ����һ��button
		Button btn = new Button(shell, SWT.NONE);
		btn.setText("�ύ");
		btn.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2,
				1));

		// ���button���¼�����
		btn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				MessageBox box = new MessageBox(shell, SWT.OK | SWT.CANCEL
						| SWT.ICON_QUESTION);
				box.setText("ȷ��");
				box.setMessage("�Ƿ�ȷ���ύ����");
				box.open();
			}
		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();
	}
}
