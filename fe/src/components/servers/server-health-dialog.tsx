import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog';
import { Card } from '@/components/ui/card';

interface ServerHealthDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  server: {
    id: string;
    name: string;
    healthScore: number;
  } | null;
}

export function ServerHealthDialog({ open, onOpenChange, server }: ServerHealthDialogProps) {
  const healthMetrics = [
    { name: 'CPU Usage', value: '45%', status: 'good' },
    { name: 'Memory Usage', value: '68%', status: 'warning' },
    { name: 'Disk Space', value: '72%', status: 'warning' },
    { name: 'Network Latency', value: '25ms', status: 'good' },
    { name: 'Active Connections', value: '234', status: 'good' },
    { name: 'Error Rate', value: '0.02%', status: 'good' },
  ];

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-2xl">
        <DialogHeader>
          <DialogTitle>Server Health Report - {server?.name}</DialogTitle>
        </DialogHeader>
        <div className="grid gap-4 py-4">
          <div className="grid grid-cols-2 gap-4">
            {healthMetrics.map((metric) => (
              <Card key={metric.name}>
                <div className="p-4">
                  <div className="flex items-center justify-between mb-2">
                    <span className="text-sm font-medium text-gray-500">{metric.name}</span>
                    <span
                      className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                        metric.status === 'good'
                          ? 'bg-green-100 text-green-800'
                          : metric.status === 'warning'
                          ? 'bg-yellow-100 text-yellow-800'
                          : 'bg-red-100 text-red-800'
                      }`}
                    >
                      {metric.status}
                    </span>
                  </div>
                  <p className="text-2xl font-semibold">{metric.value}</p>
                </div>
              </Card>
            ))}
          </div>

          <Card>
            <div className="p-4">
              <h4 className="text-sm font-medium text-gray-500 mb-2">Recent Events</h4>
              <div className="space-y-2">
                <div className="text-sm">
                  <span className="text-gray-500">2024-02-20 15:30</span> - High memory usage detected
                </div>
                <div className="text-sm">
                  <span className="text-gray-500">2024-02-20 14:15</span> - Automatic backup completed
                </div>
                <div className="text-sm">
                  <span className="text-gray-500">2024-02-20 10:00</span> - System update installed
                </div>
              </div>
            </div>
          </Card>
        </div>
      </DialogContent>
    </Dialog>
  );
}
