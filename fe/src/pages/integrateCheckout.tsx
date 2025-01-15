import { useEffect, useState } from 'react';
import axiosInstance from '../axios';

interface IntegratedCheckoutProps {
  subscriptionId: number;
}

interface Plan {
  id: string;
  name: string;
  team_size: number;
  project_size: number;
  storage: string;
  support_type: string;
  money: number; // Monthly cost
}

function IntegratedCheckout({ subscriptionId }: IntegratedCheckoutProps) {
  const [plan, setPlan] = useState<Plan | null>(null);
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');

  const onCustomerNameChange = (ev: React.ChangeEvent<HTMLInputElement>) => {
    setName(ev.target.value);
  };

  const onCustomerEmailChange = (ev: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(ev.target.value);
  };

  useEffect(() => {
    const fetchPlanDetails = async () => {
      try {
        const response = await axiosInstance.get(
          `http://localhost:8080/api/planCharge/getPlanBySubscriptionId?subscriptionId=${subscriptionId}`
        );
        setPlan(response.data);
      } catch (error) {
        console.error('Error fetching subscription plan:', error);
      }
    };
    fetchPlanDetails();
  }, [subscriptionId]);

  const createTransactionSecret = async () => {
    if (!plan) return;

    // Dynamically fill the payment data based on plan details
    const paymentData = {
      amount: plan.money, // Plan's money as the amount
      quantity: 1, // Set to 1 or adjust if needed
      currency: 'USD', // Set currency dynamically if required
      name: plan.name, // Plan name
      success_url: 'http://localhost:5173/success?session_id={CHECKOUT_SESSION_ID}', // Success URL (could be dynamic)
      cancel_url: 'http://localhost:5173/cancel?session_id={CHECKOUT_SESSION_ID}', // Cancel URL (could be dynamic)
      plan_details: plan, // Full plan details
      subscription_id: subscriptionId, // Subscription ID
      customer_email: email, // Customer email
      customer_name: name, // Customer name
    };

    try {
      const response = await axiosInstance.post('http://localhost:8080/api/v1/stripe/create-payment', paymentData);
      const sessionUrl = response.data.data.session_url; // Assuming the backend returns sessionUrl

      // Redirect the user to the Stripe Checkout session URL
      if (sessionUrl) {
        window.location.href = sessionUrl; // Redirect the user to the Stripe Checkout page
      }
    } catch (error) {
      console.error('Error creating transaction secret:', error);
    }
  };

  if (!plan) return <p className="text-center text-lg text-gray-500">Loading...</p>;

  return (
    <div className="min-h-screen flex justify-center items-center bg-gray-100 p-8">
      <div className="bg-white p-8 rounded-lg shadow-md w-full max-w-xl">
        <h2 className="text-3xl font-bold text-center mb-4">{plan.name} Checkout</h2>

        <div className="mb-4">
          <p className="text-lg">Team Size: {plan.team_size}</p>
          <p className="text-lg">Projects: {plan.project_size}</p>
          <p className="text-lg">Storage: {plan.storage}</p>
          <p className="text-lg">Support: {plan.support_type}</p>
          <p className="text-2xl font-semibold mt-2">Total: ${plan.money}</p>
        </div>

        <div className="mb-4">
          <input
            type="text"
            placeholder="Customer Name"
            value={name}
            onChange={onCustomerNameChange}
            className="w-full p-3 border border-gray-300 rounded-md mb-2"
          />
          <input
            type="email"
            placeholder="Customer Email"
            value={email}
            onChange={onCustomerEmailChange}
            className="w-full p-3 border border-gray-300 rounded-md"
          />
        </div>

        <div className="mb-4">
          <button
            onClick={createTransactionSecret}
            className="w-full p-3 bg-green-500 text-white rounded-md hover:bg-green-600"
          >
            Initiate Payment
          </button>
        </div>
      </div>
    </div>
  );
}

export default IntegratedCheckout;
