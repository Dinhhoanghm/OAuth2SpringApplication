import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';

interface RoleSelectionDialogProps {
  isOpen: boolean;
  onClose: () => void;
  onSelect: (choice: 'aiv' | 'subscription') => void;
}

export function RoleSelectionDialog({ isOpen, onClose, onSelect }: RoleSelectionDialogProps) {
  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Select Application</DialogTitle>
        </DialogHeader>
        <div className="space-y-4 mt-4">
          <Button className="w-full" onClick={() => onSelect('aiv')}>
            Go to AIV Marketplace
          </Button>
          <Button className="w-full" variant="outline" onClick={() => onSelect('subscription')}>
            Go to Subscription Management
          </Button>
        </div>
      </DialogContent>
    </Dialog>
  );
}
