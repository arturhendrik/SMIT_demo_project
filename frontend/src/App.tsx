import React, { useState, useEffect } from "react";
import TireChangeScheduler from "./TireChangeScheduler";
import getRequest from "./services/getRequest";
import { TireChangeData } from "./common/types";
import { Alert } from "react-bootstrap";

const App: React.FC = () => {
  const [data, setData] = useState<TireChangeData[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const fetchedData = await getRequest("available-times");
        setData(fetchedData);
      } catch (error) {
        setError("Failed to fetch data.");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  if (loading) return <div>Loading...</div>;
  if (error) return <Alert variant="danger">{error}</Alert>;

  return <TireChangeScheduler data={data} />;
};

export default App;
