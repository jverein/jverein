package de.jost_net.JVerein.gui.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;

import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.RadioInput;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.Color;
import de.willuhn.jameica.gui.util.ColumnLayout;
import de.willuhn.jameica.gui.util.Container;
import de.willuhn.jameica.gui.util.SimpleContainer;

public class SimpleVerticalContainer extends Container
{

  private static class GUIElementBuffer
  {

    private enum GUIElementTypes
    {
      BUTTONAREA, CHECKBOX, HEADLINE, INPUT, LABELPAIRINPUT, LABELPAIRSTRING, PART, RADIOINPUT, SEPARATOR, TEXT, TEXTCOLOR
    }

    public GUIElementTypes guiElementType;

    public List<Object> arguments;

    public GUIElementBuffer(GUIElementTypes type)
    {
      arguments = new ArrayList<>();
      guiElementType = type;
    }
  }

  List<GUIElementBuffer> GUIElements;

  private Composite comp = null;

  private int columns;

  private boolean fullSize;

  private boolean headlineFullWidth;

  private boolean arrangingStarted = false;

  /**
   * Erzeugt einen neuen Container mit cols Spalten. GUI-Elemente werden über
   * dieselben Funktionen hingefügt wie bei einem normalen Container. Jedoch
   * werden die Elemente spaltenweise angeordnet. Damit bekannt ist, wie viele
   * Elemente jede Spalte enthalten muss, werden erst beim Aufruf von Funktion
   * arrangeVertically() die Elemente gezeichnet.
   * 
   * @param parent
   *          Das Composite, in dem die Group gemalt werden soll.
   * @param fullSize
   *          true, wenn es voelle Hoehe haben soll. Sollte normalerweise false
   *          sein, da ansonsten kein Platz gespart werden kann, was jedoch die
   *          Idee vom SimpleVerticalContainer ist.
   * @param cols
   *          Anzahl der Spalten.
   */
  public SimpleVerticalContainer(Composite parent, boolean fullSize, int cols)
  {
    this(parent, fullSize, cols, true);
  }

  /**
   * Erzeugt einen neuen Container mit cols Spalten. GUI-Elemente werden über
   * dieselben Funktionen hingefügt wie bei einem normalen Container. Jedoch
   * werden die Elemente Spaltenweise angeordnet. Damit bekannt ist, wie viele
   * Elemente jede Spalte enthalten muss, werden erst bei Aufruf von Funktion
   * arrangeVertically() die Elemente gezeichnet.
   * 
   * @param parent
   *          Das Composite, in dem die Group gemalt werden soll.
   * @param fullSize
   *          true, wenn es voelle Hoehe haben soll. Sollte normalerweise false
   *          sein, da ansonsten kein Platz gespart werden kann, was jedoch die
   *          Idee vom SimpleVerticalContainer ist.
   * @param cols
   *          Anzahl der Spalten.
   * @param headlineFullWidth
   *          true, wenn die Headline über alle Spalten gezeichnet werden soll.
   *          Wenn false, wird die Headline innerhalb der Spalten gezeichnet.
   */
  public SimpleVerticalContainer(Composite parent, boolean fullSize, int cols,
      boolean headlineFullWidth)
  {
    super(fullSize);
    this.fullSize = fullSize;
    this.columns = cols;
    this.comp = parent;
    this.headlineFullWidth = headlineFullWidth;
    this.GUIElements = new ArrayList<>();
  }

  /**
   * @see de.willuhn.jameica.gui.util.Container#getComposite()
   */
  @Override
  public Composite getComposite()
  {
    return this.comp;
  }

  /**
   * Ordnet die Elemente, die mit den add...()-Funktionen angelegt wurden, in
   * Spalten an und zeichnet diese auf dem im Konstruktur angegebenen Composite.
   * arrangeVertically() sollte nur einmal aufgerufen werden. <br>
   * Nach Aufruf von arrangeVertically() sollte keine add...()-Funktionen mehr
   * verwendet werden.
   */
  public void arrangeVertically()
  {
    arrangingStarted = true;
    Composite parentComposite = this.comp;
    ColumnLayout layout = new ColumnLayout(this.comp, this.columns,
        this.fullSize);
    // create container for each column
    SimpleContainer cols[] = new SimpleContainer[this.columns];
    for (int i = 0; i < this.columns; i++)
    {
      cols[i] = new SimpleContainer(layout.getComposite());
    }
    // choose first column
    int currentCol = 0;
    comp = cols[currentCol].getComposite();

    // calculate number of items to show in each column (round up)
    int countRows = (GUIElements.size() + columns - 1) / columns;
    int currentRow = 0;

    for (GUIElementBuffer guiElement : GUIElements)
    {
      // last item in column reached? goto next column.
      if (currentRow == countRows)
      {
        currentRow = 0;
        currentCol++;
        comp = cols[currentCol].getComposite();
      }

      switch (guiElement.guiElementType)
      {
        case BUTTONAREA:
          super.addButtonArea((ButtonArea) guiElement.arguments.get(0));
          break;
        case CHECKBOX:
          super.addCheckbox((CheckboxInput) guiElement.arguments.get(0),
              (String) guiElement.arguments.get(1));
          break;
        case HEADLINE:
          if (headlineFullWidth)
          {
            // headline is handle directly by addHeadline()
          }
          else
          {
            super.addHeadline((String) guiElement.arguments.get(0));
          }
          break;
        case INPUT:
          super.addInput((Input) guiElement.arguments.get(0));
          break;
        case LABELPAIRINPUT:
          super.addLabelPair((Input) guiElement.arguments.get(0),
              (Input) guiElement.arguments.get(1));
          break;
        case LABELPAIRSTRING:
          super.addLabelPair((String) guiElement.arguments.get(0),
              (Input) guiElement.arguments.get(1));
          break;
        case PART:
          super.addPart((Part) guiElement.arguments.get(0));
          break;
        case RADIOINPUT:
          super.addRadioInput((RadioInput) guiElement.arguments.get(0),
              (String) guiElement.arguments.get(1));
          break;
        case SEPARATOR:
          super.addSeparator();
          break;
        case TEXT:
          super.addText((String) guiElement.arguments.get(0),
              (Boolean) guiElement.arguments.get(1));
          break;
        case TEXTCOLOR:
          super.addText((String) guiElement.arguments.get(0),
              (Boolean) guiElement.arguments.get(1),
              (Color) guiElement.arguments.get(2));
          break;

        default:
          break;
      }

      currentRow++;
    }
    this.comp = parentComposite;
  }

  @Override
  public void addCheckbox(CheckboxInput checkbox, String text)
  {
    if (arrangingStarted)
      super.addCheckbox(checkbox, text);

    GUIElementBuffer guiElement = new GUIElementBuffer(
        GUIElementBuffer.GUIElementTypes.CHECKBOX);
    guiElement.arguments.add(checkbox);
    guiElement.arguments.add(text);
    GUIElements.add(guiElement);
  }

  @Override
  public void addButtonArea(ButtonArea buttonArea)
  {
    if (arrangingStarted)
    {
      super.addButtonArea(buttonArea);
      return;
    }

    GUIElementBuffer guiElement = new GUIElementBuffer(
        GUIElementBuffer.GUIElementTypes.BUTTONAREA);
    guiElement.arguments.add(buttonArea);
    GUIElements.add(guiElement);
  }

  @Override
  public void addHeadline(String text)
  {
    if (arrangingStarted)
      super.addHeadline(text);

    if (headlineFullWidth)
    {
      // head lines are not part of the column layout (they are shown above
      // columns)
      super.addHeadline(text);
    }
    else
    {
      GUIElementBuffer guiElement = new GUIElementBuffer(
          GUIElementBuffer.GUIElementTypes.HEADLINE);
      guiElement.arguments.add(text);
      GUIElements.add(guiElement);
    }
  }

  @Override
  public void addInput(Input input)
  {
    if (arrangingStarted)
    {
      super.addInput(input);
      return;
    }

    GUIElementBuffer guiElement = new GUIElementBuffer(
        GUIElementBuffer.GUIElementTypes.INPUT);
    guiElement.arguments.add(input);
    GUIElements.add(guiElement);
  }

  @Override
  public void addLabelPair(Input left, Input right)
  {
    if (arrangingStarted)
    {
      super.addLabelPair(left, right);
      return;
    }

    GUIElementBuffer guiElement = new GUIElementBuffer(
        GUIElementBuffer.GUIElementTypes.LABELPAIRINPUT);
    guiElement.arguments.add(left);
    guiElement.arguments.add(right);
    GUIElements.add(guiElement);
  }

  @Override
  public void addLabelPair(String name, Input input)
  {
    if (arrangingStarted)
    {
      super.addLabelPair(name, input);
      return;
    }

    GUIElementBuffer guiElement = new GUIElementBuffer(
        GUIElementBuffer.GUIElementTypes.LABELPAIRSTRING);
    guiElement.arguments.add(name);
    guiElement.arguments.add(input);
    GUIElements.add(guiElement);
  }

  @Override
  public void addPart(Part part)
  {
    if (arrangingStarted)
    {
      super.addPart(part);
      return;
    }

    GUIElementBuffer guiElement = new GUIElementBuffer(
        GUIElementBuffer.GUIElementTypes.PART);
    guiElement.arguments.add(part);
    GUIElements.add(guiElement);
  }

  @Override
  public void addRadioInput(RadioInput radio, String text)
  {
    if (arrangingStarted)
    {
      super.addRadioInput(radio, text);
      return;
    }

    GUIElementBuffer guiElement = new GUIElementBuffer(
        GUIElementBuffer.GUIElementTypes.RADIOINPUT);
    guiElement.arguments.add(radio);
    guiElement.arguments.add(text);
    GUIElements.add(guiElement);
  }

  @Override
  public void addSeparator()
  {
    if (arrangingStarted)
    {
      super.addSeparator();

      return;
    }
    GUIElementBuffer guiElement = new GUIElementBuffer(
        GUIElementBuffer.GUIElementTypes.SEPARATOR);
    GUIElements.add(guiElement);
  }

  @Override
  public void addText(String text, boolean linewrap)
  {
    if (arrangingStarted)
    {
      super.addText(text, linewrap);
      return;
    }

    GUIElementBuffer guiElement = new GUIElementBuffer(
        GUIElementBuffer.GUIElementTypes.TEXT);
    guiElement.arguments.add(text);
    guiElement.arguments.add(linewrap);
    GUIElements.add(guiElement);
  }

  @Override
  public void addText(String text, boolean linewrap, Color color)
  {
    if (arrangingStarted)
    {
      super.addText(text, linewrap, color);
      return;
    }

    GUIElementBuffer guiElement = new GUIElementBuffer(
        GUIElementBuffer.GUIElementTypes.TEXTCOLOR);
    guiElement.arguments.add(text);
    guiElement.arguments.add(linewrap);
    guiElement.arguments.add(color);
    GUIElements.add(guiElement);
  }

  @Override
  public de.willuhn.jameica.gui.util.ButtonArea createButtonArea(int numButtons)
  {
    if (arrangingStarted)
    {
      return super.createButtonArea(numButtons);
    }

    GUI.getStatusBar().setErrorText(
        "createButtonArea(int) ist in SimpleVerticalConainer nicht implementiert!");
    return null;
  }

}
