alter table mitglied add column zahlungsrhytmus integer before blz;
 update mitglied set zahlungsrhytmus = 1;