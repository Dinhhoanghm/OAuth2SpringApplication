import { Download, RefreshCw, Activity } from "lucide-react";
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { ServerHealthDialog } from "@/components/servers/server-health-dialog";
import { useState } from "react";

interface Server {
  id: string;
  name: string;
  status: 'running' | 'stopped' | 'restarting';
  uptime: string;
  lastRestart: string;
  healthScore: number;
}

export default function ServersPage() {
  const [selectedServer, setSelectedServer] = useState<Server | null>(null);
  const [showHealthDialog, setShowHealthDialog] = useState(false);

  const servers: Server[] = [
    {
      id: '1',
      name: 'Production Server 1',
      status: 'running',
      uptime: '45 days',
      lastRestart: '2024-01-15',
      healthScore: 98
    },
    {
      id: '2',
      name: 'Production Server 2',
      status: 'running',
      uptime: '30 days',
      lastRestart: '2024-01-30',
      healthScore: 95
    },
    {
      id: '3',
      name: 'Staging Server',
      status: 'running',
      uptime: '15 days',
      lastRestart: '2024-02-15',
      healthScore: 92
    }
  ];

  const handleRestartServer = (serverId: string) => {
    // Implement server restart logic
    console.log('Restarting server:', serverId);
  };

  const handleDownloadLogs = (serverId: string) => {
    // Implement log download logic
    console.log('Downloading logs for server:', serverId);
  };

  const handleViewHealth = (server: Server) => {
    setSelectedServer(server);
    setShowHealthDialog(true);
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold">Servers</h1>
      </div>

      <div className="grid gap-4">
        {servers.map((server) => (
          <Card key={server.id}>
            <div className="p-6">
              <div className="flex items-center justify-between">
                <div className="space-y-1">
                  <h3 className="text-lg font-medium">{server.name}</h3>
                  <div className="flex items-center space-x-4 text-sm text-gray-500">
                    <div className="flex items-center">
                      <span className={`w-2 h-2 rounded-full mr-2 ${
                        server.status === 'running' ? 'bg-green-500' : 
                        server.status === 'stopped' ? 'bg-red-500' : 'bg-yellow-500'
                      }`} />
                      {server.status.charAt(0).toUpperCase() + server.status.slice(1)}
                    </div>
                    <div>Uptime: {server.uptime}</div>
                    <div>Last Restart: {server.lastRestart}</div>
                  </div>
                </div>
                <div className="flex items-center space-x-2">
                  <Button 
                    variant="outline" 
                    size="sm"
                    onClick={() => handleViewHealth(server)}
                  >
                    <Activity className="h-4 w-4 mr-2" />
                    Health Report
                  </Button>
                  <Button 
                    variant="outline" 
                    size="sm"
                    onClick={() => handleDownloadLogs(server.id)}
                  >
                    <Download className="h-4 w-4 mr-2" />
                    Download Logs
                  </Button>
                  <Button 
                    variant="outline" 
                    size="sm"
                    onClick={() => handleRestartServer(server.id)}
                  >
                    <RefreshCw className="h-4 w-4 mr-2" />
                    Restart
                  </Button>
                </div>
              </div>

              {/* Health Score Indicator */}
              <div className="mt-4">
                <div className="flex items-center justify-between mb-1">
                  <span className="text-sm font-medium">Health Score</span>
                  <span className="text-sm font-medium">{server.healthScore}%</span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-2">
                  <div 
                    className="bg-green-500 h-2 rounded-full" 
                    style={{ width: `${server.healthScore}%` }}
                  />
                </div>
              </div>
            </div>
          </Card>
        ))}
      </div>

      <ServerHealthDialog
        open={showHealthDialog}
        onOpenChange={setShowHealthDialog}
        server={selectedServer}
      />
    </div>
  );
}