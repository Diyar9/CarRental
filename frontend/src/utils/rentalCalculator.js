// Beräknar antal uthyrningsdagar mellan två datum
export const calculateRentalDays = (pickUpDate, returnDate) => {
    const start = new Date(pickUpDate);
    const end = new Date(returnDate);
    return Math.max(0, (end - start) / (1000 * 60 * 60 * 24));
};

// Beräknar total kostnad för uthyrning baserat på dagspris och datum
export const calculateRentalCost = (pricePerDay, pickUpDate, returnDate) => {
    const days = calculateRentalDays(pickUpDate, returnDate);
    return days * pricePerDay;
};
