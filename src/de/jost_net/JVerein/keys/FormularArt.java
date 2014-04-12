package de.jost_net.JVerein.keys;

public enum FormularArt
{
  SPENDENBESCHEINIGUNG(1, "Spendenbescheinigung"), RECHNUNG(2, "Rechnung"), MAHNUNG(
      3, "Mahnung"), FREIESFORMULAR(4, "Freies Formular"), SAMMELSPENDENBESCHEINIGUNG(
      5, "Sammelspendenbescheinigung"), SEPA_PRENOTIFICATION(6,
      "SEPA-Prenotification");
  private final String text;

  private final int key;

  FormularArt(int key, String text)
  {
    this.key = key;
    this.text = text;
  }

  public int getKey()
  {
    return key;
  }

  public String getText()
  {
    return text;
  }

  public static FormularArt getByKey(int key)
  {
    for (FormularArt form : FormularArt.values())
    {
      if (form.getKey() == key)
      {
        return form;
      }
    }
    return null;
  }

  @Override
  public String toString()
  {
    return getText();
  }
}
