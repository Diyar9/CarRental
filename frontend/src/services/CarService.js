import axios from 'axios';

//Bas-URL för REST API som hanterar bilar
const REST_API_BASE_URL = 'http://localhost:8080/api/cars';

//Hämtar en lista över tillgängliga bilar från API:t. @returns {Promise} - En Promise som löser sig till svaret från API:t.
export const listCars = () => axios.get(REST_API_BASE_URL);
