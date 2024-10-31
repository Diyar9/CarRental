import React, {useState, useEffect} from 'react';
import {listCars} from "../services/CarService";
import {calculateRentalCost} from '../utils/rentalCalculator';
import {Form, Button, Alert, Row, Col} from 'react-bootstrap';
import axios from 'axios';
import {validateDriverName, validateDriverAge, validateRentalDates, validateRequiredFields} from '../validators/rentalValidators';

//Komponent för att hantera uthyrning av bilar genom ett formulär.
const RentCarFormComponent = () => {
    const [cars, setCars] = useState([]); //Håller tillgängliga bilar
    const [selectedCar, setSelectedCar] = useState(''); //Vald bil
    const [pickUpDate, setPickUpDate] = useState(''); //Uthyrningsdatum
    const [returnDate, setReturnDate] = useState(''); //Returdatum
    const [driverName, setDriverName] = useState(''); //Förarens namn
    const [driverAge, setDriverAge] = useState(''); //Förarens ålder
    const [totalCost, setTotalCost] = useState(0); //Total kostnad för uthyrning
    const [errorMessage, setErrorMessage] = useState(''); //Felmeddelande vid validering
    const [confirmationMessage, setConfirmationMessage] = useState(''); //Bekräftelsemeddelande vid framgång
    const [currentStep, setCurrentStep] = useState(1); //Aktuellt steg i formuläret

    // Hämtar bilar från API:t vid montering
    useEffect(() => {
        listCars()
            .then(response => setCars(response.data))
            .catch(console.error);
    }, []);

    // Går till nästa steg i formuläret baserat på aktuella val
    const goToNextStep = () => {
        if (currentStep === 1 && selectedCar) setCurrentStep(2);
        else if (currentStep === 2 && pickUpDate && returnDate) setCurrentStep(3);
    };

    // Beräknar total kostnad baserat på vald bil och uthyrningsdatum
    const handleRentalCostCalculation = () => {
        const car = cars.find(car => car.id === parseInt(selectedCar));
        if (car && pickUpDate && returnDate) {
            setTotalCost(calculateRentalCost(car.pricePerDay, pickUpDate, returnDate));
        } else {
            setTotalCost(0);
        }
    };

    // Validerar inmatningar från användaren
    const validateInputs = () => {
        const errors = [];

        const driverNameError = validateDriverName(driverName);
        if (driverNameError) errors.push(driverNameError);

        const driverAgeError = validateDriverAge(driverAge);
        if (driverAgeError) errors.push(driverAgeError);

        const rentalDatesError = validateRentalDates(pickUpDate, returnDate);
        if (rentalDatesError) errors.push(rentalDatesError);

        const requiredFieldsError = validateRequiredFields(selectedCar, pickUpDate, returnDate, driverName, driverAge);
        if (requiredFieldsError) errors.push(requiredFieldsError);

        return errors.length ? errors : null;
    };

    // Kontrollerar bilens tillgänglighet för uthyrning
    const checkCarAvailability = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/api/rentals/cars/${selectedCar}/availability`, {
                params: {pickUpDate, returnDate},
            });
            return response.data;
        } catch (error) {
            console.error("Error checking car availability:", error);
            return false;
        }
    };

    // Hanterar formulärinskickning och uthyrning av bil
    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrorMessage("");

        const validationError = validateInputs();
        if (validationError) {
            setErrorMessage(validationError.join('\n'));
            return;
        }

        const isAvailable = await checkCarAvailability();

        if (!isAvailable) {
            setErrorMessage('Bilen är redan uthyrd under valt datumintervall.');
            return;
        }

        try {
            const rentalData = { carId: selectedCar, pickUpDate, returnDate, driverName, driverAge };
            await axios.post('http://localhost:8080/api/rentals', rentalData);

            // Visa bekräftelsemeddelande
            setConfirmationMessage('Bilen har hyrts ut!');

            // Återställ formuläret
            setSelectedCar('');
            setPickUpDate('');
            setReturnDate('');
            setDriverName('');
            setDriverAge('');
            setTotalCost(0);

            // Återställ steg till 1
            setCurrentStep(1);

            // Eventuellt uppdatera bil-listan
            listCars().then(response => setCars(response.data)).catch(console.error);
        } catch (error) {
            const message = error.response?.data?.message || 'Det gick inte att hyra ut bilen.';
            setErrorMessage(message);
        }
    };

    // Beräknar uthyrningskostnad när inmatningar ändras
    useEffect(() => {
        handleRentalCostCalculation();
    }, [selectedCar, pickUpDate, returnDate]);

    return (
        <div className="rent-car-card">
            <div className="rent-car-form">
                <h2>Hyr en bil</h2>
                {errorMessage && (
                    <Alert variant="danger">
                        {errorMessage.split('\n').map((msg, index) => (
                            <div key={index}>{msg}</div>
                        ))}
                    </Alert>
                )}
                {confirmationMessage && <Alert variant="success">{confirmationMessage}</Alert>}
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
            </div>
        </div>
    );
};

export default RentCarFormComponent;
