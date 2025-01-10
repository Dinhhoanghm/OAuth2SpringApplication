import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Label } from '@/components/ui/label';
import { Switch } from '@/components/ui/switch';

export default function NotificationSettings() {
  return (
    <Card>
      <CardHeader>
        <CardTitle>Notification Preferences</CardTitle>
      </CardHeader>
      <CardContent className="space-y-6">
        <div className="space-y-4">
          {[
            {
              title: 'Email Notifications',
              description: 'Receive email notifications for important updates',
            },
            {
              title: 'SMS Notifications',
              description: 'Get SMS alerts for critical events',
            },
            {
              title: 'Browser Notifications',
              description: 'Show desktop notifications in your browser',
            },
            {
              title: 'Marketing Communications',
              description: 'Receive updates about new features and promotions',
            },
          ].map((item) => (
            <div key={item.title} className="flex items-center justify-between">
              <div className="space-y-0.5">
                <Label>{item.title}</Label>
                <p className="text-sm text-gray-500">{item.description}</p>
              </div>
              <Switch />
            </div>
          ))}
        </div>
      </CardContent>
    </Card>
  );
}
