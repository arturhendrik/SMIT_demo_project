import { BookingData } from "../common/types";
import backendEndpoint from "../config";

const postRequest = async (data: BookingData) => {
  try {
    const response = await fetch(`${backendEndpoint}/book-time`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });

    const responseBody = await response.text();

    if (!response.ok) {
      throw new Error(responseBody || response.statusText);
    }

    return responseBody;
  } catch (error) {
    console.error("Error:", error);
    throw error;
  }
};

export default postRequest;
