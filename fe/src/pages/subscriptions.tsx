import { useState, useEffect } from 'react';
import { PlusCircle } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { formatCurrency, formatDate } from '@/lib/utils';
import axiosInstance from '../axios';

interface Plan {
  id: string;
  name: string;
  team_size: number;
  project_size: number;
  storage: string; // e.g., "20GB"
  support_type: string; // e.g., "Basic Support"
  money: number; // Monthly cost
}

export default function SubscriptionsPage() {
  const [activeSubscriptions, setActiveSubscriptions] = useState([]);
  const [plans, setPlans] = useState<Plan[]>([]);
  const [loading, setLoading] = useState(true);
  const [showAddForm, setShowAddForm] = useState(false);
  const [formData, setFormData] = useState({ user_id: '', plan_id: '', status: 'active' });
  const [updatedSubscriptions, setUpdatedSubscriptions] = useState<any>({});
  const [showUpdateForm, setShowUpdateForm] = useState(false); // Track form visibility for each subscription

  // Fetch data from backend API
  useEffect(() => {
    const fetchData = async () => {
      try {
        const subscriptionsResponse = await axiosInstance.get('/api/planUser/getAllByUser');
        setActiveSubscriptions(subscriptionsResponse.data || []);

        const plansResponse = await axiosInstance.get('/api/planCharge/getAll');
        setPlans(plansResponse.data || []);
      } catch (error) {
        console.error('Error fetching data:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const handleAddSubscription = async () => {
    try {
      const response = await axiosInstance.post('/api/planUser/insert', {
        ...formData,
      });
      setActiveSubscriptions((prev) => [...prev, response.data]);
      alert('Subscription added successfully!');
      setShowAddForm(false);
      setFormData({ user_id: '', plan_id: '', status: 'active' });
    } catch (error) {
      console.error('Error adding subscription:', error);
      alert('Failed to add subscription. Please try again.');
    }
  };
  const handleUpdateSubscriptionPlan = (id: number, newPlanId: string) => {
    const planId = Number.parseInt(newPlanId);
    console.log('planSubmit', planId);
    setUpdatedSubscriptions((prev) => ({
      ...prev,
      [id]: planId,
    }));
  };
  const handleDeleteSubscription = async (subscriptionId: number) => {
    try {
      // Replace DELETE with POST and include necessary payload
      await await axiosInstance.delete(`/api/planUser/delete?id=${subscriptionId}`);

      // Remove the deleted subscription from the state
      setActiveSubscriptions((prev) => prev.filter((sub) => sub.id !== subscriptionId));
      alert('Subscription deleted successfully!');
    } catch (error) {
      console.error('Error deleting subscription:', error);
      alert('Failed to delete subscription. Please try again.');
    }
  };

  // Save Updated Plan
  const saveUpdatedPlan = async (subscriptionId: number, planId: number, userId: number) => {
    const newPlanId = updatedSubscriptions[subscriptionId];
    console.log('newPlanId', planId);
    if (!newPlanId) {
      alert('Please select a valid plan to update.');
      return;
    }

    try {
      // Send id, user_id, and new plan_id
      const response = await axiosInstance.post(`/api/planUser/update`, {
        id: subscriptionId,
        user_id: userId,
        plan_id: newPlanId,
      });

      // Update the active subscriptions list with the new plan
      setActiveSubscriptions((prev) =>
        prev.map((sub) => (sub.id === subscriptionId ? { ...sub, plan_id: newPlanId } : sub))
      );
      alert('Plan updated successfully!');
    } catch (error) {
      console.error('Error updating plan:', error);
      alert('Failed to update the plan. Please try again.');
    }
  };
  if (loading) {
    return <p>Loading...</p>;
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-3xl font-bold tracking-tight">Subscriptions</h2>
        <Button onClick={() => setShowAddForm((prev) => !prev)}>
          <PlusCircle className="mr-2 h-4 w-4" />
          {showAddForm ? 'Cancel' : 'Add Subscription'}
        </Button>
      </div>

      {showAddForm && (
        <Card>
          <CardContent>
            <form
              onSubmit={(e) => {
                e.preventDefault();
                handleAddSubscription();
              }}
              className="space-y-4"
            >
              <div>
                <label htmlFor="plan_id" className="block text-sm font-medium text-gray-700">
                  Select Plan
                </label>
                <select
                  id="plan_id"
                  value={formData.plan_id}
                  onChange={(e) => setFormData({ ...formData, plan_id: e.target.value })}
                  required
                  className="block w-full mt-1 border-gray-300 rounded-md shadow-sm focus:border-primary focus:ring-primary"
                >
                  <option value="" disabled>
                    Choose a plan
                  </option>
                  {plans.map((plan) => (
                    <option key={plan.id} value={plan.id}>
                      {plan.name}
                    </option>
                  ))}
                </select>
              </div>
              <div className="flex justify-end space-x-2">
                <Button variant="outline" onClick={() => setShowAddForm(false)}>
                  Cancel
                </Button>
                <Button type="submit">Add Subscription</Button>
              </div>
            </form>
          </CardContent>
        </Card>
      )}
      <div className="grid gap-6">
        {/* Active Subscriptions */}
        <Card>
          <CardHeader>
            <CardTitle>Active Subscriptions</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {activeSubscriptions.length > 0 ? (
                activeSubscriptions.map((subscription: any) => {
                  const currentPlan = plans.find((p: any) => p.id === subscription.plan_id);

                  return (
                    <div
                      key={subscription.id}
                      className="p-4 border rounded-lg space-y-4 flex flex-col sm:flex-row sm:justify-between sm:items-center"
                    >
                      {/* Subscription Details */}
                      <div className="space-y-1 w-full sm:w-2/3">
                        <h3 className="text-lg font-medium">{currentPlan?.name || 'Unknown Plan'}</h3>
                        <p className="text-sm text-gray-500">
                          <strong>Status:</strong>{' '}
                          {subscription.status.charAt(0).toUpperCase() + subscription.status.slice(1)}
                        </p>
                        <p className="text-sm text-gray-500">
                          <strong>Renews on:</strong> {formatDate(subscription.currentPeriodEnd)}
                        </p>
                      </div>

                      {/* Update Plan Section */}
                      <div className="w-full sm:w-1/3">
                        {!showUpdateForm ? (
                          <Button variant="outline" size="sm" onClick={() => setShowUpdateForm(true)}>
                            Update
                          </Button>
                        ) : (
                          <form
                            onSubmit={(e) => {
                              e.preventDefault();
                              saveUpdatedPlan(subscription.id, subscription.plan_id, subscription.user_id);
                              setShowUpdateForm(false);
                            }}
                            className="space-y-2"
                          >
                            <label
                              htmlFor={`update-plan-${subscription.id}`}
                              className="block text-sm font-medium text-gray-700"
                            >
                              Select New Plan
                            </label>
                            <select
                              id={`update-plan-${subscription.id}`}
                              value={updatedSubscriptions[subscription.id] || subscription.plan_id}
                              onChange={(e) => handleUpdateSubscriptionPlan(subscription.id, e.target.value)}
                              className="block w-full mt-1 border-gray-300 rounded-md shadow-sm focus:border-primary focus:ring-primary"
                            >
                              <option value="" disabled>
                                Choose a plan
                              </option>
                              {plans.map((plan: any) => (
                                <option key={plan.id} value={plan.id}>
                                  {plan.name}
                                </option>
                              ))}
                            </select>
                            <div className="flex justify-end space-x-2">
                              <Button variant="outline" size="sm" onClick={() => setShowUpdateForm(false)}>
                                Cancel
                              </Button>
                              <Button type="submit" size="sm">
                                Save
                              </Button>
                            </div>
                          </form>
                        )}
                        <Button variant="outline" size="sm" onClick={() => handleDeleteSubscription(subscription.id)}>
                          Delete
                        </Button>
                      </div>
                    </div>
                  );
                })
              ) : (
                <p className="text-sm text-gray-500">No active subscriptions available.</p>
              )}
            </div>
          </CardContent>
        </Card>

        {/* Available Plans */}
        <Card>
          <CardHeader>
            <CardTitle>Available Plans</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="grid gap-6 md:grid-cols-3">
              {plans.map((plan: Plan) => (
                <div key={plan.id} className="rounded-lg border p-6 space-y-4 hover:border-primary transition-colors">
                  <div className="space-y-2">
                    <h3 className="font-medium text-lg">{plan.name}</h3>
                    <p className="text-3xl font-bold">
                      {formatCurrency(plan.money)}
                      <span className="text-base font-normal text-gray-500">/mo</span>
                    </p>
                    <ul className="text-sm space-y-1">
                      <li>Team Members: {plan.team_size}</li>
                      <li>Projects: {plan.project_size}</li>
                      <li>Storage: {plan.storage}</li>
                      <li>Support: {plan.support_type}</li>
                    </ul>
                  </div>
                  <Button
                    className="w-full"
                    variant={activeSubscriptions.some((s: any) => s.plan_id === plan.id) ? 'default' : 'outline'}
                    onClick={() => {
                      setFormData({ ...formData, plan_id: plan.id });
                      handleAddSubscription();
                    }}
                    disabled={activeSubscriptions.some((s: any) => s.plan_id === plan.id)}
                  >
                    {activeSubscriptions.some((s: any) => s.plan_id === plan.id) ? 'Current Plan' : 'Choose Plan'}
                  </Button>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
