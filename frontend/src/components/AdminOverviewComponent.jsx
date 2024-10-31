import React, { useEffect, useState } from 'react';
import { listRentals } from "../services/RentalService";
import { listCars } from "../services/CarService";
import { calculateRentalCost } from '../components/rentalCalculator'; // Importera kostnadsberäkningsfunktionen
import Table from 'react-bootstrap/Table';

const AdminOverviewComponent = () => {
    const [rentals, setRentals] = useState([]);
    const [cars, setCars] = useState([]);
    const [totalRevenue, setTotalRevenue] = useState(0);

    const fetchData = async () => {
        try {
            const rentalsResponse = await listRentals();
            const carsResponse = await listCars();
            setRentals(rentalsResponse.data);
            setCars(carsResponse.data);
        } catch (error) {
            console.error(error);
        }
    };

    useEffect(() => {
        fetchData();
        const intervalId = setInterval(fetchData, 5000); // Hämta data var 5:e sekund

        return () => clearInterval(intervalId);
    }, []);

    const calculateRevenue = (rental) => {
        const car = cars.find(car => car.id === rental.carId);
        return car ? calculateRentalCost(car.pricePerDay, rental.pickUpDate, rental.returnDate) : 0;
    };

    useEffect(() => {
        const total = rentals.reduce((acc, rental) => acc + calculateRevenue(rental), 0);
        setTotalRevenue(total);
    }, [rentals, cars]);

    return (
        <div>
            <Table striped bordered hover>
                <thead>
                <tr>
                    <th>Namn</th>
                    <th>Bil</th>
                    <th>Uthyrningsdatum</th>
                    <th>Returdatum</th>
                    <th>Kostnad</th>
                </tr>
                </thead>
                <tbody>
                {
                    rentals.map(rental => (
                        <tr key={rental.id}>
                            <td>{rental.driverName}</td>
                            <td>{rental.carName}</td>
                            <td>{rental.pickUpDate}</td>
                            <td>{rental.returnDate}</td>
                            <td>{calculateRevenue(rental)} SEK</td>
                        </tr>
                    ))
                }
                <tr>
                    <td colSpan="4" style={{ textAlign: 'right', fontWeight: 'bold' }}>Totala kostnad:</td>
                    <td style={{ fontWeight: 'bold' }}>{totalRevenue} SEK</td>
                </tr>
                </tbody>
            </Table>
        </div>
    );
};

export default AdminOverviewComponent;
