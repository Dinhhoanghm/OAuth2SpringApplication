import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { CreditCard, PaymentMethod } from '@/types';

export default function BillingSettings() {
  const paymentMethods: PaymentMethod[] = [
    {
      id: '1',
      cardBrand: 'visa',
      last4: '4242',
      expiryMonth: 12,
      expiryYear: 2024,
      isDefault: true,
    },
  ];

  return (
    <div className="space-y-6">
      <Card>
        <CardHeader>
          <CardTitle>Payment Methods</CardTitle>
        </CardHeader>
        <CardContent className="space-y-6">
          <div className="space-y-4">
            {paymentMethods.map((method) => (
              <div key={method.id} className="flex items-center justify-between p-4 border rounded-lg">
                <div className="flex items-center space-x-4">
                  <div className="space-y-1">
                    <p className="font-medium">
                      {method.cardBrand.toUpperCase()} •••• {method.last4}
                    </p>
                    <p className="text-sm text-gray-500">
                      Expires {method.expiryMonth}/{method.expiryYear}
                    </p>
                  </div>
                </div>
                {method.isDefault && <span className="text-sm text-gray-500">Default</span>}
              </div>
            ))}
          </div>
          <Button variant="outline">Add Payment Method</Button>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Billing History</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="text-sm text-gray-500">No billing history available</div>
        </CardContent>
      </Card>
    </div>
  );
}
