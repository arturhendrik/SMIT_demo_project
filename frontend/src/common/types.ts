export type TireChangeData = {
  id: string;
  time: string;
  name: string;
  address: string;
  vehicleTypes: string[];
};

export type BookingData = {
  id: string;
  contactInformation: string;
  name: string;
};

export type DateRange = {
  start: string;
  end: string;
};

export interface TireChangeSchedulerProps {
  data: TireChangeData[];
  onBookingSuccess: () => void;
}
