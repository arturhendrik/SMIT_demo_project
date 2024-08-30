import React, { useState, useEffect } from "react";
import TireChangeScheduler from "./components/TireChangeScheduler";
import getRequest from "./services/getRequest";
import { TireChangeData } from "./common/types";
import { Alert } from "react-bootstrap";

const App: React.FC = () => {
  const [data, setData] = useState<TireChangeData[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const fetchData = async () => {
    try {
      const fetchedData = await getRequest("available-times");
      setData(fetchedData);
    } catch (error) {
      setError("Failed to fetch data: " + error);
    } finally {
      setLoading(false);
    }
  };

  const handleBookingSuccess = () => {
    fetchData();
  };

  useEffect(() => {
    fetchData();
  }, []);

  if (loading) return <div>Loading...</div>;
  if (error) return <Alert variant="danger">{error}</Alert>;

  return (
    <TireChangeScheduler data={data} onBookingSuccess={handleBookingSuccess} />
  );
};

export default App;
