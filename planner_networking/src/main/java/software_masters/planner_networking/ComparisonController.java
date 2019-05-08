package software_masters.planner_networking;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;

public class ComparisonController
{
	@FXML
	private Button refresh;
	@FXML
	ChoiceBox<PlanFile> yearDropdown;
	@FXML
	private Button yearSelect;
	@FXML
	private Button exit;
	@FXML
	private TreeView<Node> tree1;
	@FXML
	private TreeView<Node> tree2;
	@FXML
	private Button add1;
	@FXML
	private Button remove1;
	@FXML
	private TextField content1;
	@FXML
	private Button add2;
	@FXML
	private Button remove2;
	@FXML
	private TextField content2;
	@FXML
	private Button edit1;
	@FXML
	private Button edit2;
	ViewTransitionalModel vtmodel;
	ChangeListener<String> listener1;
	ChangeListener<String> listener2;
	private Client client;
	private TreeItem<Node> currNode1;
	private TreeItem<Node> currNode2;
	private String year1;
	
	public void setViewTransitionalModel(ViewTransitionalModel model)
	{
		this.vtmodel = model;
	}

	public void setClient(Client client)
	{

		this.client = client;
	}
	
	public void setYear1(String year)
	{
		year1=year;
	}

	@FXML
	void addBranch1(MouseEvent event) throws Exception
	{
		addBranch(currNode1, year1);
	}
	
	@FXML
	void exit(MouseEvent event) throws IOException, Exception
	{
		vtmodel.showMainView();
	}
	
	@FXML
	void addBranch2(MouseEvent event) throws Exception
	{
		addBranch(currNode2, yearDropdown.getValue().getYear());
	}
	
	public void addBranch(TreeItem<Node> currNode, String year) throws Exception
	{
		client.getPlan1(year);
		client.setCurrNode(currNode.getValue().getChildren().get(0));
		client.addBranch();
		TreeItem<Node> tree2 = new TreeItem<Node>(
				currNode.getValue().getChildren().get(currNode.getValue().getChildren().size() - 1));
		recursiveSearch(tree2);
		currNode.getChildren().add(tree2);
	}
	
	/**
	 * used to recursive search through a graph to create a TreeView
	 * 
	 * @param parent
	 */
	
	private static void recursiveSearch(TreeItem<Node> parent)
	{
		if (parent.getValue().getChildren().size() != 0)
		{

			for (int i = 0; i < parent.getValue().getChildren().size(); i++)
			{
				TreeItem<Node> tree = new TreeItem<Node>(parent.getValue().getChildren().get(i));
				tree.setExpanded(true);
				parent.getChildren().add(tree);
				recursiveSearch(tree);
			}
		}

	}
	
	@FXML
	void edit1(MouseEvent event) throws IllegalArgumentException, RemoteException
	{
		edit1();
	}
	
	public void edit1() throws IllegalArgumentException, RemoteException
	{
		client.getPlan1(year1);
		Boolean bool = client.getCurrPlanFile().isCanEdit();

		if ((edit1.getText() == "Edit") && bool)
		{
			edit1.setText("View");
			content1.setEditable(true);
		} else if (edit1.getText() == "View")
		{
			edit1.setText("Edit");
			content1.setEditable(false);
			add1.setDisable(true);
			remove1.setDisable(true);
		}

	}
	
	@FXML
	void edit2(MouseEvent event) throws IllegalArgumentException, RemoteException
	{
		edit2();
	}
	public void edit2() throws IllegalArgumentException, RemoteException
	{
		client.getPlan2(yearDropdown.getValue().getYear());
		Boolean bool = client.getCurrPlanFile2().isCanEdit();

		if ((edit2.getText() == "Edit") && bool)
		{
			edit2.setText("View");
			content2.setEditable(true);
		} else if (edit2.getText() == "View")
		{
			edit2.setText("Edit");
			content2.setEditable(false);
			add2.setDisable(true);
			remove2.setDisable(true);
		}

	}
	
	@FXML
	void remove1(MouseEvent event)
	{
		if (currNode1.getParent().getChildren().size() > 1)
		{
			client.setCurrNode(currNode1.getValue());
			client.getCurrNode1().getParent().removeChild(client.getCurrNode1());
			currNode1.getParent().getChildren().remove(currNode1);
		}
	}
	
	@FXML
	void remove2(MouseEvent event)
	{
		if (currNode2.getParent().getChildren().size() > 1)
		{
			client.setCurrNode(currNode2.getValue());
			client.getCurrNode2().getParent().removeChild(client.getCurrNode2());
			currNode2.getParent().getChildren().remove(currNode2);
		}
	}
	
	public void planChange1() throws IllegalArgumentException, RemoteException
	{
		edit1.setDisable(false);
		tree1.setRoot(makeTree1(year1).getRoot());

		tree1.getSelectionModel().selectedItemProperty()
				.addListener((v, oldValue, newValue) -> tree_SelectionChanged1(newValue));
		edit1.setText("View");
		edit1();
	}
	
	@FXML
	void planChange2(MouseEvent event) throws IllegalArgumentException, RemoteException
	{
		planChange2();
		edit2(event);
	}
	
	public void planChange2() throws IllegalArgumentException, RemoteException
	{
		System.out.println("Changing plans");
		edit2.setDisable(false);
		tree2.setRoot(makeTree2(yearDropdown.getValue().getYear()).getRoot());

		tree2.getSelectionModel().selectedItemProperty()
				.addListener((v, oldValue, newValue) -> tree_SelectionChanged2(newValue));
		edit2.setText("View");
		edit2();
	}
	
	void tree_SelectionChanged1(TreeItem<Node> item)
	{
		if (listener1 != null)
		{
			content1.textProperty().removeListener(listener1);
		}

		if (item != null)
		{
			String str = item.getValue().getData();
			content1.setText(str);
			this.currNode1 = item;
			if (edit1.getText().contentEquals("View"))
			{
				add1.setDisable(false);
				remove1.setDisable(false);
			}
		}
		listener1 = (observable, oldvalue, newvalue) -> setText2(newvalue);
		content1.textProperty().addListener(listener1);

	}
	
	void tree_SelectionChanged2(TreeItem<Node> item)
	{
		if (listener2 != null)
		{
			content2.textProperty().removeListener(listener2);
		}

		if (item != null)
		{
			String str = item.getValue().getData();
			content2.setText(str);
			this.currNode2 = item;
			if (edit2.getText().contentEquals("View"))
			{
				add2.setDisable(false);
				remove2.setDisable(false);
			}
		}
		listener2 = (observable, oldvalue, newvalue) -> setText2(newvalue);
		content2.textProperty().addListener(listener2);

	}
	
	void setText1(String newvalue)
	{
		currNode1.getValue().setData(newvalue);
	}
	
	void setText2(String newvalue)
	{
		currNode2.getValue().setData(newvalue);
	}
	
	public void getPlans(ChoiceBox<PlanFile> plans) throws Exception
	{
		plans.getItems().addAll(client.getPlans());
	}
	public TreeView<Node> makeTree1(String year) throws IllegalArgumentException, RemoteException
	{

		client.getPlan1(year);
		Node nodeRoot = client.getCurrNode1();
		TreeItem<Node> treeRoot = new TreeItem<Node>(nodeRoot);
		treeRoot.setExpanded(true);
		recursiveSearch(treeRoot);
		return new TreeView<Node>(treeRoot);
	}
	
	public TreeView<Node> makeTree2(String year) throws IllegalArgumentException, RemoteException
	{
 
		client.getPlan2(year);
		Node nodeRoot = client.getCurrNode2();
		TreeItem<Node> treeRoot = new TreeItem<Node>(nodeRoot);
		treeRoot.setExpanded(true);
		recursiveSearch(treeRoot);
		return new TreeView<Node>(treeRoot);
	}
	
	private void showTrees()
	{
		
	}
	
	

	@FXML
	void refresh() throws IllegalArgumentException, RemoteException
	{
		Node a=tree1.getRoot().getValue();
		Node b=tree2.getRoot().getValue();
		a=clearDifferences(a);
		b=clearDifferences(b);
		Pair<Node,Node> p=detectDifferences(a,b);
		tree1.setRoot(new TreeItem<Node>(p.getKey()));
		tree2.setRoot(new TreeItem<Node>(p.getValue()));
		client.getCurrPlanFile().getPlan().setRoot(tree1.getRoot().getValue());
		client.getCurrPlanFile2().getPlan().setRoot(tree2.getRoot().getValue());
		TreeItem<Node> ti=new TreeItem<Node>(client.getCurrPlanFile().getPlan().getRoot());
		recursiveSearch(ti);
		tree1.setRoot(ti);;
		ti=new TreeItem<Node>(client.getCurrPlanFile2().getPlan().getRoot());
		recursiveSearch(ti);
		tree2.setRoot(ti);;
	}
	
	private Node clearDifferences(Node a)
	{
		if(a.getName().substring(0,4).equals("D - "))
		{
			a.setName(a.getName().substring(4));
		}
		for(Node child:a.getChildren())
		{
			clearDifferences(child);
		}
		return a;
	}
	
	private Pair<Node,Node> detectDifferences(Node a,Node b)
	{
		if(a==null&&b==null)
		{
			return null;
		}
		if(a==null)
		{
			b.setName("D - "+b.getName());
			return null;
		}
		if(b==null)
		{
			a.setName("D - "+a.getName());
			return null;
		}
		if(!a.getData().equals(b.getData()))
		{
			a.setName("D - "+a.getName());
			b.setName("D - "+b.getName());
		}
		int max=0;
		if(a.getChildren().size()>b.getChildren().size())
		{
			max=a.getChildren().size();
		}
		else
		{
			max=b.getChildren().size();
		}
		for(int i=0;i<max;i++)
		{
			if(a.getChildren().size()-1<i)
			{
				detectDifferences(null,b.getChildren().get(i));
			}
			else if(b.getChildren().size()-1<i)
			{
				detectDifferences(a.getChildren().get(i),null);
			}
			else
			{
				detectDifferences(a.getChildren().get(i),b.getChildren().get(i));
			}
		}
		return new Pair<Node,Node>(a,b);
	}
	
}
