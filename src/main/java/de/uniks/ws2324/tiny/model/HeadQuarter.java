package de.uniks.ws2324.tiny.model;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;

public class HeadQuarter
extends City {
   public static final String PROPERTY_MONEY = "money";
   public static final String PROPERTY_CARS = "cars";
   private int money;
   private List<Car> cars;

   public int getMoney()
   {
      return this.money;
   }

   public HeadQuarter setMoney(int value)
   {
      if (value == this.money)
      {
         return this;
      }

      final int oldValue = this.money;
      this.money = value;
      this.firePropertyChange(PROPERTY_MONEY, oldValue, value);
      return this;
   }

   public List<Car> getCars()
   {
      return this.cars != null ? Collections.unmodifiableList(this.cars) : Collections.emptyList();
   }

   public HeadQuarter withCars(Car value)
   {
      if (this.cars == null)
      {
         this.cars = new ArrayList<>();
      }
      if (!this.cars.contains(value))
      {
         this.cars.add(value);
         value.setOwner(this);
         this.firePropertyChange(PROPERTY_CARS, null, value);
      }
      return this;
   }

   public HeadQuarter withCars(Car... value)
   {
      for (final Car item : value)
      {
         this.withCars(item);
      }
      return this;
   }

   public HeadQuarter withCars(Collection<? extends Car> value)
   {
      for (final Car item : value)
      {
         this.withCars(item);
      }
      return this;
   }

   public HeadQuarter withoutCars(Car value)
   {
      if (this.cars != null && this.cars.remove(value))
      {
         value.setOwner(null);
         this.firePropertyChange(PROPERTY_CARS, value, null);
      }
      return this;
   }

   public HeadQuarter withoutCars(Car... value)
   {
      for (final Car item : value)
      {
         this.withoutCars(item);
      }
      return this;
   }

   public HeadQuarter withoutCars(Collection<? extends Car> value)
   {
      for (final Car item : value)
      {
         this.withoutCars(item);
      }
      return this;
   }

   @Override
   public void removeYou()
   {
      super.removeYou();
      this.withoutCars(new ArrayList<>(this.getCars()));
   }
}
