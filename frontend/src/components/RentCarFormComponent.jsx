import React, { useState, useEffect } from 'react';
import { listCars } from "../services/CarService";
import { calculateRentalCost } from '../components/rentalCalculator';
import { Form, Button, Alert, Row, Col } from 'react-bootstrap';
import axios from 'axios';

const RentCarFormComponent = () => {
    const [cars, setCars] = useState([]);
    const [selectedCar, setSelectedCar] = useState('');
    const [pickUpDate, setPickUpDate] = useState('');
    const [returnDate, setReturnDate] = useState('');
    const [driverName, setDriverName] = useState('');
    const [driverAge, setDriverAge] = useState('');
    const [totalCost, setTotalCost] = useState(0);
    const [errorMessage, setErrorMessage] = useState('');
    const [currentStep, setCurrentStep] = useState(1);

    useEffect(() => {
        listCars()
            .then(response => setCars(response.data))
            .catch(console.error);
    }, []);

    useEffect(() => {
        if (selectedCar && pickUpDate && returnDate) calculateRentalCostHandler();
    }, [selectedCar, pickUpDate, returnDate]);

    const calculateRentalCostHandler = () => {
        const car = cars.find(car => car.id === parseInt(selectedCar));
        if (car) {
            setTotalCost(calculateRentalCost(car.pricePerDay, pickUpDate, returnDate));
        } else {
            setTotalCost(0);
        }
    };

    const checkCarAvailability = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/api/rentals/cars/${selectedCar}/availability`, {
                params: {
                    pickUpDate,
                    returnDate,
                },
            });
            console.log("Availability check response:", response.data); // Se till att detta visar rätt data
            return response.data; // Förutsätter att API returnerar en boolean
        } catch (error) {
            console.error("Error checking car availability:", error);
            return false; // Returnera false om API-anropet misslyckas
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrorMessage("");

        if (!selectedCar || !pickUpDate || !returnDate || !driverName || !driverAge) {
            setErrorMessage('Alla fält måste vara ifyllda.');
            return;
        }

        if (!/^[a-zA-Z\s]+$/.test(driverName.trim())) {
            setErrorMessage('Förarnamn får endast innehålla bokstäver och mellanslag.');
            return;
        }

        if (parseInt(driverAge) < 18) {
            setErrorMessage('Föraren måste vara minst 18 år.');
            return;
        }

        if (new Date(pickUpDate) >= new Date(returnDate)) {
            setErrorMessage('Returdatum måste vara efter uthyrningsdatum.');
            return;
        }

        // Kontrollera bilens tillgänglighet
        const isAvailable = await checkCarAvailability();
        if (!isAvailable) {
            setErrorMessage('Bilen är redan uthyrd under valt datumintervall.');
            return;
        }

        try {
            const rentalData = { carId: selectedCar, pickUpDate, returnDate, driverName, driverAge };
            await axios.post('http://localhost:8080/api/rentals', rentalData);
            alert('Bilen har hyrts ut!');
        } catch (error) {
            const message = error.response?.data?.message || 'Det gick inte att hyra ut bilen.';
            setErrorMessage(message);
        }
    };

    const goToNextStep = () => {
        if (currentStep === 1 && selectedCar) setCurrentStep(2);
        else if (currentStep === 2 && pickUpDate && returnDate) setCurrentStep(3);
    };
    return (
        <div className="rent-car-card">
            <div className="rent-car-form">
                <h2>Hyr en bil</h2>
                {errorMessage && <Alert variant="danger">{errorMessage}</Alert>}
                <Form onSubmit={handleSubmit}>
                    <div className="stepper">
                        <div className={`step ${currentStep === 1 ? 'active' : ''}`}>
                            <div className="step-number">1</div>
                            <div className="step-label">Välj bil</div>
                        </div>
                        <div className={`step ${currentStep === 2 ? 'active' : ''}`}>
                            <div className="step-number">2</div>
                            <div className="step-label">Hyrdatum</div>
                        </div>
                        <div className={`step ${currentStep === 3 ? 'active' : ''}`}>
                            <div className="step-number">3</div>
                            <div className="step-label">Information</div>
                        </div>
                    </div>

                    {currentStep === 1 && (
                        <Row>
                            <Col md={12}>
                                <Form.Group controlId="carSelect">
                                    <Form.Label>Välj bil</Form.Label>
                                    <Form.Control as="select" value={selectedCar}
                                                  onChange={e => setSelectedCar(e.target.value)} required>
                                        <option value="">Välj en bil...</option>
                                        {cars.map(car => (
                                            <option key={car.id} value={car.id}>
                                                {car.name}, {car.pricePerDay} kr/dag
                                            </option>
                                        ))}
                                    </Form.Control>
                                </Form.Group>
                                <Button variant="primary" onClick={goToNextStep} className="button-spacing">
                                    Nästa
                                </Button>
                            </Col>
                        </Row>
                    )}

                    {currentStep === 2 && (
                        <>
                            <Row>
                                <Col md={6}>
                                    <Form.Group controlId="pickUpDate">
                                        <Form.Label>Uthyrningsdatum</Form.Label>
                                        <Form.Control
                                            type="date"
                                            value={pickUpDate}
                                            min={new Date().toISOString().split("T")[0]}
                                            onChange={e => setPickUpDate(e.target.value)}
                                            required
                                        />
                                    </Form.Group>
                                </Col>
                                <Col md={6}>
                                    <Form.Group controlId="returnDate">
                                        <Form.Label>Returdatum</Form.Label>
                                        <Form.Control
                                            type="date"
                                            value={returnDate}
                                            min={pickUpDate}
                                            onChange={e => setReturnDate(e.target.value)}
                                            required
                                        />
                                    </Form.Group>
                                </Col>
                            </Row>
                            <Button variant="primary" onClick={() => setCurrentStep(1)} className="button-spacing">
                                Tillbaka
                            </Button>
                            <Button variant="primary" onClick={goToNextStep} className="button-spacing">
                                Nästa
                            </Button>
                        </>
                    )}

                    {currentStep === 3 && (
                        <>
                            <Row>
                                <Col md={6}>
                                    <Form.Group controlId="driverName">
                                        <Form.Label>Förarens namn</Form.Label>
                                        <Form.Control
                                            type="text"
                                            value={driverName}
                                            onChange={e => setDriverName(e.target.value)}
                                            required
                                        />
                                    </Form.Group>
                                </Col>
                                <Col md={6}>
                                    <Form.Group controlId="driverAge">
                                        <Form.Label>Förarens ålder</Form.Label>
                                        <Form.Control
                                            type="number"
                                            min="18"
                                            value={driverAge}
                                            onChange={e => setDriverAge(e.target.value)}
                                            required
                                        />
                                    </Form.Group>
                                </Col>
                            </Row>
                            <h5>Kostnad: {totalCost} SEK</h5>
                            <Button variant="primary" onClick={() => setCurrentStep(2)} className="button-spacing">
                                Tillbaka
                            </Button>
                            <Button variant="primary" type="submit" className="button-spacing">
                                Hyra bil
                            </Button>
                        </>
                    )}
                </Form>

                <style jsx>{`
                    .rent-car-form {
                        max-width: 500px;
                        margin: auto;
                        padding: 20px;
                        background-color: #E7F3F3;
                        border-radius: 8px;
                        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.4);
                    }

                    .stepper {
                        display: flex;
                        margin-bottom: 1rem;
                        align-items: center;
                    }

                    .step {
                        display: flex;
                        flex-direction: column;
                        align-items: center;
                        flex: 1;
                    }

                    .step-number {
                        width: 30px;
                        height: 30px;
                        border-radius: 50%;
                        background: #d8d8d8;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        font-weight: bold;
                        color: #fff;
                        margin-bottom: 0.5rem;
                    }

                    .step.active .step-number {
                        background: #669086;
                    }

                    .button-spacing {
                        margin-top: 10px;
                        margin-right: 10px;
                        background-color: #669086;
                        border-color: #669086;
                        color: white;
                    }
                    .button-spacing:hover {
                        background-color: #557a6f;
                        border-color: #557a6f;
                    }
                `}</style>
            </div>
        </div>
    );
};

export default RentCarFormComponent;
