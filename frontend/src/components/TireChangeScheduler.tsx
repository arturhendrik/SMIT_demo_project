import React, { useState } from "react";
import {
  Table,
  Form,
  Row,
  Col,
  Container,
  Button,
  Alert,
} from "react-bootstrap";
import {
  DateRange,
  TireChangeData,
  TireChangeSchedulerProps,
} from "../common/types";
import postRequest from "../services/postRequest";

const TireChangeScheduler: React.FC<TireChangeSchedulerProps> = ({
  data,
  onBookingSuccess,
}) => {
  const [selectedShop, setSelectedShop] = useState<string>("");
  const [dateRange, setDateRange] = useState<DateRange>({ start: "", end: "" });
  const [selectedVehicleType, setSelectedVehicleType] = useState<string>("");
  const [submitMessage, setSubmitMessage] = useState<string | null>(null);
  const [showAlert, setShowAlert] = useState(false);

  const handleShopChange = (e: React.ChangeEvent<any>) =>
    setSelectedShop(e.target.value);
  const handleDateChange = (e: React.ChangeEvent<any>) => {
    const { name, value } = e.target;

    if (
      name === "start" &&
      dateRange.end &&
      new Date(value) > new Date(dateRange.end)
    ) {
      setDateRange({ ...dateRange, start: value, end: "" });
    } else if (
      name === "end" &&
      dateRange.start &&
      new Date(value) < new Date(dateRange.start)
    ) {
      alert("End date cannot be earlier than start date.");
    } else {
      setDateRange({ ...dateRange, [name]: value });
    }
  };
  const handleVehicleTypeChange = (e: React.ChangeEvent<any>) =>
    setSelectedVehicleType(e.target.value);

  const filterData = (): TireChangeData[] => {
    return data.filter((item) => {
      const isShopMatch = selectedShop ? item.name === selectedShop : true;

      const normalizeDate = (dateString: string) => {
        const date = new Date(dateString);
        date.setHours(0, 0, 0, 0);
        return date;
      };

      const isDateMatch = (() => {
        const itemDate = normalizeDate(item.time);
        if (dateRange.start && dateRange.end) {
          return (
            itemDate >= normalizeDate(dateRange.start) &&
            itemDate <= normalizeDate(dateRange.end)
          );
        } else if (dateRange.start) {
          return itemDate >= normalizeDate(dateRange.start);
        } else if (dateRange.end) {
          return itemDate <= normalizeDate(dateRange.end);
        }
        return true;
      })();

      const isVehicleTypeMatch = selectedVehicleType
        ? item.vehicleTypes.includes(selectedVehicleType)
        : true;

      return isShopMatch && isDateMatch && isVehicleTypeMatch;
    });
  };

  const filteredData = filterData();

  const handleBooking = async (id: string, name: string) => {
    try {
      const contactInformation = "artur.hendrik@gmail.com";
      const result = await postRequest({ id, contactInformation, name });
      setSubmitMessage(`Success: ${result}`);
      onBookingSuccess();
    } catch (error) {
      if (error instanceof Error) {
        setSubmitMessage(`Failed to book: ${error.message}`);
      } else {
        setSubmitMessage("Failed to book: An unknown error occurred.");
      }
    }
    setShowAlert(true);
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  return (
    <Container>
      <h1 className="my-4">Tire Change Scheduler</h1>
      {submitMessage && showAlert && (
        <Alert
          variant={submitMessage.startsWith("Success") ? "success" : "danger"}
          dismissible
          onClose={() => setShowAlert(false)}
        >
          {submitMessage}
        </Alert>
      )}
      <Form>
        <Row className="mb-3">
          <Col md={3}>
            <Form.Group controlId="formShop">
              <Form.Label>Shop</Form.Label>
              <Form.Control
                as="select"
                value={selectedShop}
                onChange={handleShopChange}
              >
                <option value="">All Shops</option>
                {[...new Set(data.map((item) => item.name))].map((shop) => (
                  <option key={shop} value={shop}>
                    {shop}
                  </option>
                ))}
              </Form.Control>
            </Form.Group>
          </Col>
          <Col md={3}>
            <Form.Group controlId="formStartDate">
              <Form.Label>Start Date</Form.Label>
              <Form.Control
                type="date"
                name="start"
                value={dateRange.start}
                onChange={handleDateChange}
              />
            </Form.Group>
          </Col>
          <Col md={3}>
            <Form.Group controlId="formEndDate">
              <Form.Label>End Date</Form.Label>
              <Form.Control
                type="date"
                name="end"
                value={dateRange.end}
                onChange={handleDateChange}
              />
            </Form.Group>
          </Col>
          <Col md={3}>
            <Form.Group controlId="formVehicleType">
              <Form.Label>Vehicle Type</Form.Label>
              <Form.Control
                as="select"
                value={selectedVehicleType}
                onChange={handleVehicleTypeChange}
              >
                <option value="">All Vehicle Types</option>
                {[...new Set(data.flatMap((item) => item.vehicleTypes))].map(
                  (type) => (
                    <option key={type} value={type}>
                      {type}
                    </option>
                  )
                )}
              </Form.Control>
            </Form.Group>
          </Col>
        </Row>
      </Form>
      <Table striped bordered hover className="mt-4">
        <thead>
          <tr>
            <th>Shop</th>
            <th>Address</th>
            <th>Time</th>
            <th>Vehicle Types</th>
            <th>Booking</th>
          </tr>
        </thead>
        <tbody>
          {filteredData.length > 0 ? (
            filteredData.map((item) => (
              <tr key={item.id}>
                <td>{item.name}</td>
                <td>{item.address}</td>
                <td>{new Date(item.time).toLocaleString()}</td>
                <td>{item.vehicleTypes.join(", ")}</td>
                <td>
                  <Button
                    variant="primary"
                    onClick={() => handleBooking(item.id, item.name)}
                  >
                    Book
                  </Button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan={5} className="text-center">
                No results found
              </td>
            </tr>
          )}
        </tbody>
      </Table>
    </Container>
  );
};

export default TireChangeScheduler;
