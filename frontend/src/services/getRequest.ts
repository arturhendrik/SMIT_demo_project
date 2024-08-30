import backendEndpoint from "../config";

const getRequest = async (requestEndPoint: string) => {
  try {
    let url = `${backendEndpoint}/${requestEndPoint}`;

    const requestOptions = {
      method: "GET",
    };

    const response = await fetch(url, requestOptions);

    if (!response.ok) {
      throw new Error(response.statusText);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error:", error);
    throw error;
  }
};

export default getRequest;
